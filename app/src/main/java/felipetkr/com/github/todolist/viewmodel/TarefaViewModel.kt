package felipetkr.com.github.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import felipetkr.com.github.todolist.data.Tarefa
import felipetkr.com.github.todolist.data.TarefaDatabase
import felipetkr.com.github.todolist.repository.TarefaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TarefaViewModel(private val repository: TarefaRepository) : ViewModel() {

    val tarefas: StateFlow<List<Tarefa>> = repository.listarTarefas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    fun adicionarTarefa(tarefa: Tarefa) {
        viewModelScope.launch { repository.inserirTarefa(tarefa) }
    }

    fun editarTarefa(tarefa: Tarefa) {
        viewModelScope.launch { repository.atualizarTarefa(tarefa) }
    }

    fun excluirTarefa(tarefa: Tarefa) {
        viewModelScope.launch { repository.excluirTarefa(tarefa) }
    }

    fun alternarConclusao(tarefa: Tarefa) {
        viewModelScope.launch {
            repository.atualizarTarefa(tarefa.copy(concluida = !tarefa.concluida))
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val dao = TarefaDatabase.getDatabase(context.applicationContext).tarefaDao()
                TarefaViewModel(TarefaRepository(dao))
            }
        }
    }
}
