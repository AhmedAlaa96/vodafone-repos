package com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoUiResponse
import com.ahmed.vodafonerepos.data.repositories.getrepositorieslist.IGetRepositoriesListRepository
import com.ahmed.vodafonerepos.ui.base.BaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetRepositoriesListUseCase @Inject constructor(private val repository: IGetRepositoriesListRepository) :
    BaseUseCase(repository), IGetRepositoriesListUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getRepositoriesList(pageModel: PageModel): Flow<Status<RepoUiResponse>> {
        return repository.getRepositoriesList()
            .mapLatest {
                mapMoviesListStatus(it, pageModel)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRepositoriesListLocally(pageModel: PageModel): Flow<Status<RepoUiResponse>> {
        return repository.getReposListLocally(pageModel)
            .mapLatest(::mapMoviesListLocally)
            .flatMapLatest {
                if (it.isOfflineData() || it.isSuccess()) {
                    return@flatMapLatest flowOf(it)
                } else {
                    getRepositoriesList(pageModel)
                }
            }
    }


    private fun mapMoviesListLocally(
        reposListResponse: Status<ArrayList<RepoResponse>>,
    ): Status<RepoUiResponse> {
        return when (validateResponse(reposListResponse)) {
            StatusCode.VALID -> {
                if (reposListResponse.data.isNullOrEmpty())
                    Status.NoData(error = "No Data")
                else {
                    Status.OfflineData(
                        RepoUiResponse(reposListResponse.data.size, reposListResponse.data),
                        null
                    )
                }
            }
            else -> {
                Status.CopyStatus(reposListResponse, RepoUiResponse())
            }
        }
    }

    private fun mapMoviesListStatus(
        reposListResponse: Status<ArrayList<RepoResponse>>,
        pageModel: PageModel
    ): Status<RepoUiResponse> {
        return when (validateResponse(reposListResponse)) {
            StatusCode.VALID -> {
                if (reposListResponse.data.isNullOrEmpty())
                    Status.NoData(error = "No Data")
                else {
                    repository.insertReposListLocally(reposListResponse.data)

                    Status.Success(
                        RepoUiResponse(
                            reposListResponse.data.size,
                            reposListResponse.data.subList(
                                getFromIndex(pageModel.page-1, reposListResponse.data.size),
                                getToIndex(pageModel.page, reposListResponse.data.size)
                            ).toCollection(
                                arrayListOf()
                            )
                        )
                    )
                }
            }
            else -> {
                Status.CopyStatus(reposListResponse, RepoUiResponse())
            }
        }
    }

    private fun getFromIndex(page: Int, listSize: Int): Int {
       return if ((listSize - (page * 10)) < 10) listSize - 1
        else
            (page * 10)
    }

    private fun getToIndex(page: Int, listSize: Int): Int {
      return  if ((listSize - (page * 10)) < 10) listSize - 1
        else
            (page * 10) - 1
    }
}