package com.connor.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.connor.tasks.ui.dialog.AddTask
import com.connor.tasks.ui.theme.TasksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasksTheme {
                // A surface container using the 'background' color from the theme
                NavExample()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavExample() {
    val navController = rememberNavController()
    val item = listOf(Screen.Profile, Screen.Dashboard)
    var openDialog by remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                item.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigateSingleTopTo(screen.route)
                        })
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Add Task") },
                icon = { Icon(Icons.Filled.Add, "Localized Description") },
                onClick = { openDialog = true },
                elevation = FloatingActionButtonDefaults.loweredElevation()
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Profile.route,
            Modifier.padding(it)
        ) {
            composable(Screen.Profile.route) {
                Profile(navController = navController)
            }
            composable(Screen.Dashboard.route) {
                Dashboard(navController = navController)
            }
        }
        if (openDialog) {
            AddTask(onDismiss = { openDialog = false }, cancelIconClick = { openDialog = false }) {

            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Profile : Screen("profile", R.string.profile)
    object Dashboard : Screen("dashboard", R.string.dashboard)
}

@Composable
fun Profile(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ConstraintLayout {
            val (btnAdd, tvText) = createRefs()
            Button(
                onClick = {
                    navController.navigateSingleTopTo(Screen.Dashboard.route)
                },
                modifier = Modifier.constrainAs(btnAdd) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
            ) {
                Text(text = "Go To Dashboard!")
            }
            Text(text = "Hello Profile!", modifier = Modifier.constrainAs(tvText) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            })
        }
    }
}

@Composable
fun Dashboard(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Text(text = "Hello Dashboard!")
            Button(onClick = {
                navController.navigateSingleTopTo(Screen.Profile.route)
            }) {
                Text(text = "Go To Profile!")
            }
        }

    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id)
        launchSingleTop = true
    }

@Composable
fun Greeting(name: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(text = "Hello $name!")
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TasksTheme {
        Greeting("Android")
    }
}