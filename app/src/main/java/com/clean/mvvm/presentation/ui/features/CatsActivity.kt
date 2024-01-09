package com.clean.mvvm.presentation.ui.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.clean.mvvm.presentation.ui.features.catDetails.view.CatFullDetail
import com.clean.mvvm.presentation.ui.features.catDetails.viewModel.CatsDetailsViewModel
import com.clean.mvvm.presentation.ui.features.cats.navigation.Destinations
import com.clean.mvvm.presentation.ui.features.cats.navigation.NavigationActions
import com.clean.mvvm.presentation.ui.features.cats.navigation.NavigationScreens
import com.clean.mvvm.presentation.ui.features.cats.view.CatScreen
import com.clean.mvvm.presentation.ui.features.cats.viewModel.CatsViewModel
import com.clean.mvvm.presentation.ui.theme.ComposeSampleTheme
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                LBGSingleActivityApp()
            }
        }
    }

    @Composable
    fun LBGSingleActivityApp() {
        val sharedViewModel: SharedViewModel by viewModel()

        /**
         * using the rememberNavController()
         * to get the instance of the navController
         */
        val navController = rememberNavController()
        val actions = remember(navController) { NavigationActions(navController) }
        NavHost(
            navController = navController,
            startDestination = NavigationScreens.LBGHome.screenRoute,
        ) {
            composable(NavigationScreens.LBGHome.screenRoute) {
                CatsDestination(navController, sharedViewModel, actions.openFullScreen)
            }
            composable(
                route = NavigationScreens.FullScreenView.screenRoute,
                arguments = listOf(
                    navArgument(Destinations.FullDetailArgs.CatId) {
                        type = NavType.StringType; defaultValue = ""
                    },
                    navArgument(Destinations.FullDetailArgs.CatUrl) {
                        type = NavType.StringType;defaultValue = ""
                    },
                    navArgument(Destinations.FullDetailArgs.favInitialState) {
                        type = NavType.BoolType;defaultValue = false
                    }

                )
            ) {
                val arguments = requireNotNull(it.arguments)
                val catId = arguments.getString(Destinations.FullDetailArgs.CatId) ?: ""
                val catUrl = arguments.getString(Destinations.FullDetailArgs.CatUrl) ?: ""
                val favInitialState =
                    arguments.getBoolean(Destinations.FullDetailArgs.favInitialState) ?: false

                CatsFullView(
                    catUrl.trim(),
                    catId,
                    favInitialState,
                    sharedViewModel,
                    /*refreshFavCats = {//Navigation Result API:
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            Constants.RESULT,
                            it
                        )
                    },*/
                    actions.navigateBack
                )
            }

        }

    }


    @Composable
    fun CatsDestination(
        navController: NavController,// its useful if you are using Navigation Result API: for handling back navigation
        sharedViewModel: SharedViewModel,
        openFullScreen: (String, String, Boolean) -> Unit
    ) {
        val viewModel: CatsViewModel by viewModel()
//        var isApiCallNeeded by remember { mutableStateOf(true) }//Navigation Result API:
        CatScreen(
            state = viewModel.state.collectAsState().value,
            effectFlow = viewModel.effects.receiveAsFlow(),
            onNavigationRequested = { itemUrl, imageId, isFav ->
                openFullScreen(imageId, itemUrl, isFav)
            },
            onRefreshCall = {
                viewModel.getCatsData()

            })

        // Collect changes in counterState and execute a method when it changes
        LaunchedEffect(sharedViewModel.hasUpdatedState) {
            val collectJob = launch {
                sharedViewModel.hasUpdatedState.collect { newState ->
                    if (newState) {
                        viewModel.getFavCatsData()
                        sharedViewModel.updateCounterState(false)
                    }
                }
            }
        }


//Navigation Result API:
        /*  val currentBackStackEntry by navController.currentBackStackEntryAsState()
          currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(Constants.RESULT)
              ?.observe(this) {
                  if (it && isApiCallNeeded) {
                      viewModel.getFavCatsData()
                      isApiCallNeeded = false
                  }
              }*/

    }

    @Composable
    private fun CatsFullView(
        url: String,
        catId: String,
        favInitialState: Boolean,
        sharedViewModel: SharedViewModel,
//        refreshFavCats: (Boolean) -> Unit,//Navigation Result API:
        navigateBack: () -> Unit,
        viewModel: CatsDetailsViewModel = koinViewModel()
    ) {


        viewModel.checkFav(catId)
//        val initialState = remember { viewModel.isFavourite.value }
        val isFavourite by viewModel.isFavourite.collectAsState()

        CatFullDetail(
            url = url,
            onBackPressed = {
//                refreshFavCats(initialState != isFavourite) //Navigation Result API:
                if (favInitialState != isFavourite)
                    sharedViewModel.updateCounterState(true)
                navigateBack()
            },
            isFavourite = isFavourite,
            favSelection = {
                viewModel.updateFavouriteState(it)
                if (it) {
                    viewModel.postFavCatData()
                } else viewModel.deleteFavCatData()
            }
        )

    }
}