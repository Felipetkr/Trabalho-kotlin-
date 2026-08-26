package felipetkr.com.github.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import felipetkr.com.github.todolist.navigation.AppNavigation
import felipetkr.com.github.todolist.ui.theme.FiaptodolistTheme
import felipetkr.com.github.todolist.viewmodel.TarefaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiaptodolistTheme {
                val tarefaViewModel: TarefaViewModel = viewModel(
                    factory = TarefaViewModel.factory(applicationContext)
                )
                AppNavigation(viewModel = tarefaViewModel)
            }
        }
    }
}
