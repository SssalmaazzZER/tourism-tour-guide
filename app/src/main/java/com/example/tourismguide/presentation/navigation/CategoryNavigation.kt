package com.example.tourismguide.presentation.navigation

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.example.tourismguide.R

/**
 * Single source of truth for category routing.
 * Home chips, Discover grid, and drawer menu all use these routes.
 */
object CategoryNavigation {

    data class CategoryRoute(
        val contentType: String?,
        @StringRes val titleRes: Int
    )

    val allCategories: List<CategoryRoute> = listOf(
        CategoryRoute("CITY", R.string.nav_cities),
        CategoryRoute("MONUMENT", R.string.nav_monuments),
        CategoryRoute("CULTURE", R.string.nav_culture),
        CategoryRoute("ARCHITECTURE", R.string.nav_architecture),
        CategoryRoute("MUSIC", R.string.nav_music),
        CategoryRoute("GASTRONOMY", R.string.nav_gastronomy),
        CategoryRoute("FESTIVAL", R.string.nav_festivals),
        CategoryRoute("ARTISANAT", R.string.nav_artisanat),
        CategoryRoute("NATURE", R.string.nav_nature),
        CategoryRoute("ACTIVITY", R.string.nav_activities),
        CategoryRoute("MUSEUM", R.string.nav_museums),
        CategoryRoute("UNESCO", R.string.nav_unesco),
        CategoryRoute("EVENT", R.string.nav_events),
        CategoryRoute("CIRCUIT", R.string.nav_circuits)
    )

    val homeQuickCategories: List<CategoryRoute> = listOf(
        CategoryRoute(null, R.string.category_all),
        CategoryRoute("CITY", R.string.nav_cities),
        CategoryRoute("MONUMENT", R.string.nav_monuments),
        CategoryRoute("CULTURE", R.string.nav_culture),
        CategoryRoute("GASTRONOMY", R.string.nav_gastronomy),
        CategoryRoute("NATURE", R.string.nav_nature),
        CategoryRoute("ACTIVITY", R.string.nav_activities),
        CategoryRoute("FESTIVAL", R.string.nav_festivals)
    )

    fun routeForDrawerItem(itemId: Int): CategoryRoute? = when (itemId) {
        R.id.nav_cities -> CategoryRoute("CITY", R.string.nav_cities)
        R.id.nav_monuments -> CategoryRoute("MONUMENT", R.string.nav_monuments)
        R.id.nav_culture -> CategoryRoute("CULTURE", R.string.nav_culture)
        R.id.nav_architecture -> CategoryRoute("ARCHITECTURE", R.string.nav_architecture)
        R.id.nav_music -> CategoryRoute("MUSIC", R.string.nav_music)
        R.id.nav_gastronomy -> CategoryRoute("GASTRONOMY", R.string.nav_gastronomy)
        R.id.nav_festivals -> CategoryRoute("FESTIVAL", R.string.nav_festivals)
        R.id.nav_artisanat -> CategoryRoute("ARTISANAT", R.string.nav_artisanat)
        R.id.nav_nature -> CategoryRoute("NATURE", R.string.nav_nature)
        R.id.nav_activities -> CategoryRoute("ACTIVITY", R.string.nav_activities)
        R.id.nav_museums -> CategoryRoute("MUSEUM", R.string.nav_museums)
        R.id.nav_unesco -> CategoryRoute("UNESCO", R.string.nav_unesco)
        R.id.nav_events -> CategoryRoute("EVENT", R.string.nav_events)
        R.id.nav_circuits -> CategoryRoute("CIRCUIT", R.string.nav_circuits)
        R.id.nav_favorites -> CategoryRoute("FAVORITE", R.string.favorites)
        else -> null
    }

    fun navigateToCategory(navController: NavController, route: CategoryRoute) {
        if (route.contentType == null) return
        navController.navigate(
            R.id.categoryBrowseFragment,
            bundleFor(route)
        )
    }

    fun bundleFor(route: CategoryRoute): Bundle = Bundle().apply {
        putString("contentType", route.contentType)
        putInt("titleRes", route.titleRes)
    }
}
