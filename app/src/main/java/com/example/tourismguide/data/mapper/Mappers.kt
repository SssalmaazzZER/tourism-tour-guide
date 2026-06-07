package com.example.tourismguide.data.mapper

import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.remote.dto.PlaceDto
import com.example.tourismguide.domain.model.Place

fun PlaceEntity.toDomain() = Place(
    id = id,
    name = name,
    description = description,
    category = category,
    latitude = latitude,
    longitude = longitude,
    imageUrl = imageUrl,
    rating = rating,
    isSaved = isSaved
)

fun PlaceDto.toEntity(cachedAt: Long = System.currentTimeMillis()) = PlaceEntity(
    id = id,
    name = name,
    description = description,
    category = category,
    latitude = latitude,
    longitude = longitude,
    imageUrl = imageUrl,
    rating = rating,
    reviewCount = 0,
    address = "",
    placeType = "attraction",
    isSaved = false,
    cachedAt = cachedAt,
    lastAccessedAt = 0L
)
