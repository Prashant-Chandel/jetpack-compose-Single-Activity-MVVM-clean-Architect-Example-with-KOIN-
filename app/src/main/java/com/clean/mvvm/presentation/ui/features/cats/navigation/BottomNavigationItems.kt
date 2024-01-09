package com.clean.mvvm.presentation.ui.features.cats.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.clean.mvvm.R
import com.clean.mvvm.utils.Constants


data class BottomNavigationItem(
    val title: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val screenRoute: String = ""
)

fun getBottomNavigationItems(context: Context): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = context.getString(R.string.home),
            icon = Icons.Filled.Home,
            screenRoute = BottomNavigationScreens.Home.screenRoute
        ),
        BottomNavigationItem(
            title = context.getString(R.string.my_favorites),
            icon = Icons.Filled.Favorite,
            screenRoute = BottomNavigationScreens.MyFavorites.screenRoute
        )
    )
}

sealed class BottomNavigationScreens(var screenRoute: String) {
    data object Home : BottomNavigationScreens(Constants.HOME_ROUTES)
    data object MyFavorites : BottomNavigationScreens(Constants.MY_FAVOURITES_ROUTES)
}
