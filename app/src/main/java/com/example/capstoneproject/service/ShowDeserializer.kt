package com.example.capstoneproject.service

import com.example.capstoneproject.model.DataModel
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ShowDeserializer : JsonDeserializer<DataModel> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DataModel {
        val jsonObject = json.asJsonObject

        val rank = jsonObject.get("rank").asInt
        val title = jsonObject.get("title").asString
        val description = jsonObject.get("description").asString
        val image = jsonObject.get("image").asString
        val bigImage = jsonObject.get("big_image").asString
        val genre = jsonObject.get("genre").asJsonArray.map { it.asString }
        val thumbnail = jsonObject.get("thumbnail").asString
        val rating = jsonObject.get("rating").asString
        val id = jsonObject.get("id").asString
        val imdbid = jsonObject.get("imdbid").asString
        val imdbLink = jsonObject.get("imdb_link").asString

        val yearElement = jsonObject.get("year")
        val year = if (yearElement.isJsonPrimitive) {
            if (yearElement.asJsonPrimitive.isNumber) {
                yearElement.asInt.toString()
            } else {
                yearElement.asString
            }
        } else {
            yearElement.asString
        }

        return DataModel(rank, title, description, image, bigImage, genre, thumbnail, rating, id, year, imdbid, imdbLink)
    }
}