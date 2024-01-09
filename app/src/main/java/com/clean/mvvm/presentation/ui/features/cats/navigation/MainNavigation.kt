package com.clean.mvvm.presentation.ui.features.cats.navigation

import androidx.navigation.NavHostController
import com.clean.mvvm.utils.encodedString

object Destinations {
    internal const val Home = "LBGHome"
    internal const val FullScreen = "FullDetails"

    object FullDetailArgs {
        internal const val CatId = "CatId"
        internal const val CatUrl = "CatUrl"
        internal const val favInitialState = "FavInitialState"
    }
}


class NavigationActions(navController: NavHostController) {
    val openFullScreen: (String, String, Boolean) -> Unit = { catId, catUrl, isFav ->
        navController.navigate(
            NavigationScreens.FullScreenView.createRoute(
                catId,
                catUrl,
                isFav
            )
        )
    }
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}

sealed class NavigationScreens(var screenRoute: String) {
    data object LBGHome : NavigationScreens(Destinations.Home)


    data object FullScreenView :
        NavigationScreens("${Destinations.FullScreen}/${Destinations.FullDetailArgs.CatUrl}={${Destinations.FullDetailArgs.CatUrl}}/{${Destinations.FullDetailArgs.CatId}}/{${Destinations.FullDetailArgs.favInitialState}}") {
        fun createRoute(catId: String, catUrl: String, isFavFlow: Boolean) =
            "${Destinations.FullScreen}/${Destinations.FullDetailArgs.CatUrl}= ${
                encodedString(
                    catUrl
                )
            }/$catId/$isFavFlow"
    }
}

