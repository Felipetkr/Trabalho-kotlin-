package felipetkr.com.github.todolist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import felipetkr.com.github.todolist.data.Tarefa
import felipetkr.com.github.todolist.viewmodel.TarefaViewModel

@Composable
fun ListaTarefasScreen(
    viewModel: TarefaViewModel,
    aoCriarTarefa: () -> Unit,
    aoAbrirTarefa: (Int) -> Unit
) {
    val tarefas by viewModel.tarefas.collectAsStateWithLifecycle()

    ListaTarefasConteudo(
        tarefas = tarefas,
        aoCriarTarefa = aoCriarTarefa,
        aoAbrirTarefa = aoAbrirTarefa,
        aoAlternarConclusao = viewModel::alternarConclusao,
        aoExcluir = viewModel::excluirTarefa
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTarefasConteudo(
    tarefas: List<Tarefa>,
    aoCriarTarefa: () -> Unit,
    aoAbrirTarefa: (Int) -> Unit,
    aoAlternarConclusao: (Tarefa) -> Unit,
    aoExcluir: (Tarefa) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Minhas Tarefas") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = aoCriarTarefa) {
                Icon(Icons.Default.Add, contentDescription = "Nova tarefa")
            }
        }
    ) { paddingInterno ->
        if (tarefas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma tarefa cadastrada ainda.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = tarefas, key = { it.id }) { tarefa ->
                    CartaoTarefa(
                        tarefa = tarefa,
                        aoAlternarConclusao = { aoAlternarConclusao(tarefa) },
                        aoAbrir = { aoAbrirTarefa(tarefa.id) },
                        aoExcluir = { aoExcluir(tarefa) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartaoTarefa(
    tarefa: Tarefa,
    aoAlternarConclusao: () -> Unit,
    aoAbrir: () -> Unit,
    aoExcluir: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = aoAbrir)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarefa.concluida,
                onCheckedChange = { aoAlternarConclusao() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarefa.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (tarefa.concluida) TextDecoration.LineThrough else TextDecoration.None
                )
                if (tarefa.descricao.isNotBlank()) {
                    Text(
                        text = tarefa.descricao,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = aoExcluir) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir tarefa")
            }
        }
    }
}

private val tarefasDeExemplo = listOf(
    Tarefa(id = 1, titulo = "Estudar para a prova", descricao = "Revisar Room e Compose", concluida = false),
    Tarefa(id = 2, titulo = "Enviar atividade FIAP", descricao = "Upload no portal da FIAP", concluida = true)
)

@Preview(showBackground = true, name = "Lista com tarefas")
@Composable
private fun ListaTarefasConteudoPreview() {
    ListaTarefasConteudo(
        tarefas = tarefasDeExemplo,
        aoCriarTarefa = {},
        aoAbrirTarefa = {},
        aoAlternarConclusao = {},
        aoExcluir = {}
    )
}

@Preview(showBackground = true, name = "Lista vazia")
@Composable
private fun ListaTarefasConteudoVaziaPreview() {
    ListaTarefasConteudo(
        tarefas = emptyList(),
        aoCriarTarefa = {},
        aoAbrirTarefa = {},
        aoAlternarConclusao = {},
        aoExcluir = {}
    )
}

@Preview(showBackground = true, name = "Cartao pendente")
@Composable
private fun CartaoTarefaPendentePreview() {
    CartaoTarefa(
        tarefa = tarefasDeExemplo[0],
        aoAlternarConclusao = {},
        aoAbrir = {},
        aoExcluir = {}
    )
}

@Preview(showBackground = true, name = "Cartao concluido")
@Composable
private fun CartaoTarefaConcluidaPreview() {
    CartaoTarefa(
        tarefa = tarefasDeExemplo[1],
        aoAlternarConclusao = {},
        aoAbrir = {},
        aoExcluir = {}
    )
}
