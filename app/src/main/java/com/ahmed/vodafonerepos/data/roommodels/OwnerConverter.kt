package com.ahmed.vodafonerepos.data.roommodels

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.ahmed.vodafonerepos.data.models.dto.Owner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class OwnerConverter {

    @TypeConverter
    fun fromOwnerToString(owner: Owner?): String? {
        return Gson().toJson(owner)
    }

    @TypeConverter
    fun fromStringToOwner(json: String?): Owner? {
        return Gson().fromJson(json, Owner::class.java)
    }
}