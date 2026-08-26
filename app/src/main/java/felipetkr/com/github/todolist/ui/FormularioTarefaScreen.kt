package felipetkr.com.github.todolist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import felipetkr.com.github.todolist.data.Tarefa
import felipetkr.com.github.todolist.viewmodel.TarefaViewModel

private const val ID_NOVA_TAREFA = 0

@Composable
fun FormularioTarefaScreen(
    viewModel: TarefaViewModel,
    tarefaId: Int,
    aoVoltar: () -> Unit
) {
    val tarefas by viewModel.tarefas.collectAsStateWithLifecycle()
    val tarefaEmEdicao = tarefas.find { it.id == tarefaId }

    FormularioConteudo(
        estaEditando = tarefaId != ID_NOVA_TAREFA,
        tituloInicial = tarefaEmEdicao?.titulo.orEmpty(),
        descricaoInicial = tarefaEmEdicao?.descricao.orEmpty(),
        aoSalvar = { titulo, descricao ->
            if (tarefaEmEdicao != null) {
                viewModel.editarTarefa(tarefaEmEdicao.copy(titulo = titulo, descricao = descricao))
            } else {
                viewModel.adicionarTarefa(Tarefa(titulo = titulo, descricao = descricao))
            }
            aoVoltar()
        },
        aoVoltar = aoVoltar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioConteudo(
    estaEditando: Boolean,
    tituloInicial: String,
    descricaoInicial: String,
    aoSalvar: (titulo: String, descricao: String) -> Unit,
    aoVoltar: () -> Unit
) {
    var titulo by rememberSaveable(tituloInicial) { mutableStateOf(tituloInicial) }
    var descricao by rememberSaveable(descricaoInicial) { mutableStateOf(descricaoInicial) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (estaEditando) "Editar tarefa" else "Nova tarefa") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { aoSalvar(titulo.trim(), descricao.trim()) },
                enabled = titulo.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (estaEditando) "Salvar alterações" else "Adicionar tarefa")
            }
        }
    }
}

@Preview(showBackground = true, name = "Nova tarefa")
@Composable
private fun FormularioConteudoNovaPreview() {
    FormularioConteudo(
        estaEditando = false,
        tituloInicial = "",
        descricaoInicial = "",
        aoSalvar = { _, _ -> },
        aoVoltar = {}
    )
}

@Preview(showBackground = true, name = "Editar tarefa")
@Composable
private fun FormularioConteudoEdicaoPreview() {
    FormularioConteudo(
        estaEditando = true,
        tituloInicial = "Estudar para a prova",
        descricaoInicial = "Revisar Room e Compose",
        aoSalvar = { _, _ -> },
        aoVoltar = {}
    )
}
