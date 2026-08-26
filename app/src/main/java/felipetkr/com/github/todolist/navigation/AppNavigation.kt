package felipetkr.com.github.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import felipetkr.com.github.todolist.ui.FormularioTarefaScreen
import felipetkr.com.github.todolist.ui.ListaTarefasScreen
import felipetkr.com.github.todolist.viewmodel.TarefaViewModel

private const val ARG_TAREFA_ID = "tarefaId"
private const val ROTA_LISTA = "lista"
private const val ROTA_FORMULARIO = "formulario/{$ARG_TAREFA_ID}"

private fun rotaFormulario(tarefaId: Int) = "formulario/$tarefaId"

@Composable
fun AppNavigation(viewModel: TarefaViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROTA_LISTA) {
        composable(ROTA_LISTA) {
            ListaTarefasScreen(
                viewModel = viewModel,
                aoCriarTarefa = { navController.navigate(rotaFormulario(0)) },
                aoAbrirTarefa = { tarefaId -> navController.navigate(rotaFormulario(tarefaId)) }
            )
        }
        composable(
            route = ROTA_FORMULARIO,
            arguments = listOf(navArgument(ARG_TAREFA_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val tarefaId = backStackEntry.arguments?.getInt(ARG_TAREFA_ID) ?: 0
            FormularioTarefaScreen(
                viewModel = viewModel,
                tarefaId = tarefaId,
                aoVoltar = { navController.popBackStack() }
            )
        }
    }
}
