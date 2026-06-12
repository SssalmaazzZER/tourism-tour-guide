package com.example.tourismguide.data.mapper

import com.example.tourismguide.data.local.entity.CircuitEntity
import com.example.tourismguide.data.local.entity.CircuitStepEntity
import com.example.tourismguide.data.local.entity.ContentIndexEntity
import com.example.tourismguide.data.local.entity.ContentRichDetailEntity
import com.example.tourismguide.domain.model.Circuit
import com.example.tourismguide.domain.model.CircuitStep
import com.example.tourismguide.domain.model.ContentLeader
import com.example.tourismguide.domain.model.ContentRichDetail
import com.example.tourismguide.domain.model.TourismContent
import java.util.UUID
import org.json.JSONArray

fun ContentIndexEntity.toDomain() = TourismContent(
    id = id,
    contentType = contentType,
    title = title,
    subtitle = subtitle,
    description = description,
    imageUrl = imageUrl,
    latitude = latitude,
    longitude = longitude,
    regionId = regionId,
    cityId = cityId,
    category = category,
    rating = rating,
    popularity = popularity,
    priceLabel = priceLabel
)

fun CircuitEntity.toDomain(steps: List<CircuitStepEntity> = emptyList()) = Circuit(
    id = id,
    name = name,
    description = description,
    circuitType = circuitType,
    durationDays = durationDays,
    imageUrl = imageUrl,
    steps = steps.map { it.toDomain() }
)

fun CircuitStepEntity.toDomain() = CircuitStep(
    id = id,
    stepOrder = stepOrder,
    title = title,
    description = description,
    referenceType = referenceType,
    referenceId = referenceId,
    latitude = latitude,
    longitude = longitude
)

fun ContentRichDetailEntity.toDomain() = ContentRichDetail(
    contentId = contentId,
    richBody = richBody,
    galleryUrls = galleryUrls.split("|").filter { it.isNotBlank() },
    leaders = parseLeaders(leadersJson)
)

private fun parseLeaders(json: String): List<ContentLeader> {
    if (json.isBlank()) return emptyList()
    return runCatching {
        val array = JSONArray(json)
        buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                add(
                    ContentLeader(
                        name = obj.optString("name"),
                        role = obj.optString("role"),
                        bio = obj.optString("bio"),
                        imageUrl = obj.optString("imageUrl")
                    )
                )
            }
        }
    }.getOrDefault(emptyList())
}

fun newFavoriteId() = UUID.randomUUID().toString()
