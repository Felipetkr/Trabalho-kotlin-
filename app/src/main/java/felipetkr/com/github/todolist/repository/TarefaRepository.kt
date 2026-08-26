package felipetkr.com.github.todolist.repository

import felipetkr.com.github.todolist.data.Tarefa
import felipetkr.com.github.todolist.data.TarefaDao
import kotlinx.coroutines.flow.Flow

class TarefaRepository(private val tarefaDao: TarefaDao) {

    fun listarTarefas(): Flow<List<Tarefa>> = tarefaDao.listarTodas()

    suspend fun inserirTarefa(tarefa: Tarefa) = tarefaDao.inserir(tarefa)

    suspend fun atualizarTarefa(tarefa: Tarefa) = tarefaDao.atualizar(tarefa)

    suspend fun excluirTarefa(tarefa: Tarefa) = tarefaDao.deletar(tarefa)
}
