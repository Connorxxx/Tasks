package com.connor.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.connor.tasks.model.room.TasksEntity
import com.connor.tasks.type.TasksType
import com.connor.tasks.type.onInsert
import com.connor.tasks.ui.dialog.AddTask
import com.connor.tasks.ui.theme.TasksTheme
import com.connor.tasks.utils.logd
import com.connor.tasks.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasksTheme {
                // A surface container using the 'background' color from the theme
                NavExample(viewModel)

            }
        }
        initScope()
    }

    private fun initScope() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch(Dispatchers.IO) {
                    viewModel.dao.collect { type ->
                        with(type) {
                            onInsert { viewModel.insertTask(it) }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavExample(vm: MainViewModel) {
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
                Profile(vm, navController = navController)
            }
            composable(Screen.Dashboard.route) {
                Dashboard(navController = navController)
            }
        }
        if (openDialog) {
            AddTask(
                vm = vm,
                onDismiss = { openDialog = false },
                cancelIconClick = { openDialog = false }) {
                vm.sendTask(TasksType.Insert(TasksEntity(vm.text)))
                openDialog = false
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Profile : Screen("profile", R.string.profile)
    object Dashboard : Screen("dashboard", R.string.dashboard)
}

@Composable
fun Profile(vm: MainViewModel, navController: NavHostController) {
    val list = vm.loadTasks.collectAsState(initial = emptyList()).value
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list) { task ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (check, tvID, tvTask) = createRefs()
                    Text(
                        text = task.id.toString(), 
                        modifier = Modifier.constrainAs(tvID) {
                        start.linkTo(parent.start, margin = 12.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                        fontSize = 24.sp
                    )
                    Text(
                        text = task.task,
                        fontSize = 24.sp,
                        modifier = Modifier.constrainAs(tvTask) {
                            start.linkTo(tvID.end, margin = 12.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.constrainAs(check) {
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }


//        ConstraintLayout {
//            val (btnAdd, tvText) = createRefs()
//            Button(
//                onClick = {
//                    navController.navigateSingleTopTo(Screen.Dashboard.route)
//                },
//                modifier = Modifier.constrainAs(btnAdd) {
//                    bottom.linkTo(parent.bottom, margin = 16.dp)
//                    start.linkTo(parent.start, margin = 16.dp)
//                }
//            ) {
//                Text(text = "Go To Dashboard!")
//            }
//            Text(text = "Hello Profile!", modifier = Modifier.constrainAs(tvText) {
//                top.linkTo(parent.top, margin = 16.dp)
//                start.linkTo(parent.start, margin = 16.dp)
//            })
//        }
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
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
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