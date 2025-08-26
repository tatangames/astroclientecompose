package com.tatanstudios.astropollocliente.vistas.principal.carrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tatanstudios.astropollocliente.extras.TokenManager
import kotlinx.coroutines.flow.first
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomModal1BotonTitulo
import com.tatanstudios.astropollocliente.model.modelos.ModeloInformacionProductoArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloInformacionProductoEditarArray

import com.tatanstudios.astropollocliente.viewmodel.InformacionProductoEditadoViewModel
import java.util.Locale
import kotlin.collections.first

@Composable
fun EditarProductoScreen(
    navController: NavHostController,
    idFilaCarrito: Int,
    viewModel: InformacionProductoEditadoViewModel = viewModel(),
    //viewModelGuardar: EditarProductoCarritoViewModel = viewModel(),   // ← VM para guardar cambios
) {
    val ctx = LocalContext.current
    val tokenManager = remember { TokenManager(ctx) }

    // Carga de info del producto a editar
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    // Guardar cambios
    //val isLoadingGuardar by viewModelGuardar.isLoading.observeAsState(false)
    //val resultadoGuardar by viewModelGuardar.resultado.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }
    var modalTituloString by remember { mutableStateOf("") }

    // Estado de datos
    var producto by remember { mutableStateOf<ModeloInformacionProductoEditarArray?>(null) }
    var idusuario by remember { mutableStateOf("") }

    // Estado UI (inicializados al recibir el producto)
    var cantidad by remember { mutableStateOf(1) }
    var nota by remember { mutableStateOf("") }
    var errorNotaObligatoria by remember { mutableStateOf(false) }
    var datosInicializados by remember { mutableStateOf(false) }

    // cargar datos
    LaunchedEffect(idFilaCarrito) {
        idusuario = tokenManager.idUsuario.first()
        viewModel.informacionProductoEditarRetrofit(idusuario, idFilaCarrito)
    }

    // manejar resultado retrofit (carga)
    resultado?.getContentIfNotHandled()?.let { res ->
        if (res.success == 1) {
            producto = res.producto
            // Setear estados iniciales sólo una vez
            res.producto?.let { p ->
                if (!datosInicializados) {
                    cantidad = (p.cantidad).coerceAtLeast(1)                   // viene del JSON
                    // toma 'nota' si tu backend la manda; si no, usa nota_producto
                    nota = (p.nota ?: p.notaProducto ?: "").trim()
                    datosInicializados = true
                }
            }
        } else {
            CustomToasty(
                ctx,
                ctx.getString(R.string.error_reintentar_de_nuevo),
                ToastType.ERROR
            )
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.editar_cantidad),
                colorResource(R.color.colorRojo)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            producto?.let { prod ->
                // precio unitario (String -> Double)
                val precioUnit = prod.precio?.toDoubleOrNull() ?: 0.0
                val total = precioUnit * cantidad

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // IMAGEN
                    item {
                        val tieneImagen = prod.utilizaImagen == 1 && !prod.imagen.isNullOrBlank()
                        if (tieneImagen) {
                            AsyncImage(
                                model = ImageRequest.Builder(ctx)
                                    .data(/* TU_URL_BASE + */ prod.imagen)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = prod.nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(R.drawable.spinloading),
                                error = painterResource(R.drawable.camaradefecto)
                            )
                        }
                    }

                    // NOMBRE
                    item {
                        Text(
                            text = prod.nombre ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = colorResource(R.color.colorLetraRoja)
                        )
                    }

                    // DESCRIPCIÓN
                    item {
                        val desc = (prod.descripcion ?: "")
                            .replace("\r\n", "\n")
                            .replace("\\r\\n", "\n")
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    item { Spacer(Modifier.height(6.dp)) }

                    // PRECIO UNITARIO
                    item {
                        Text(
                            text = stringResource(R.string.precio) + ": " + formatearUSD(precioUnit),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // STEPPER CANTIDAD (inicia con la del carrito)
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalIconButton(
                                onClick = { if (cantidad > 1) cantidad-- },
                                enabled = cantidad > 1,
                                modifier = Modifier.size(44.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = colorResource(R.color.colorLetraRoja),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Menos")
                            }

                            Text(
                                text = cantidad.toString(),
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = MaterialTheme.typography.titleMedium
                            )

                            FilledTonalIconButton(
                                onClick = { if (cantidad < 50) cantidad++ },
                                enabled = cantidad < 50,
                                modifier = Modifier.size(44.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = colorResource(R.color.colorLetraRoja),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Más")
                            }
                        }
                    }

                    // NOTAS
                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.notas),
                                style = MaterialTheme.typography.labelLarge
                            )
                            TextField(
                                value = nota,
                                onValueChange = {
                                    errorNotaObligatoria = false
                                    nota = if (it.length <= 300) it else it.take(300)
                                },
                                placeholder = { Text(stringResource(R.string.nota_para_este_producto)) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = false,
                                maxLines = 3,
                                isError = errorNotaObligatoria,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = colorResource(R.color.colorAzul),
                                    unfocusedIndicatorColor = Color.Gray,
                                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                )
                            )
                            if (errorNotaObligatoria) {
                                Text(
                                    text = stringResource(R.string.nota_es_requerida),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    // TOTAL
                    item {
                        Text(
                            text = formatearUSD(total),
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    item { Spacer(Modifier.height(15.dp)) }

                    // BOTÓN GUARDAR CAMBIOS
                    item {
                        Button(
                            onClick = {
                                // validar nota obligatoria si utiliza_nota == 1
                                if (prod.utilizaNota == 1 && nota.isBlank()) {
                                    errorNotaObligatoria = true
                                    CustomToasty(
                                        ctx,
                                        ctx.getString(R.string.nota_es_requerida),
                                        ToastType.WARNING
                                    )
                                    return@Button
                                }

                                val cantidadElegida = cantidad
                                val notaElegida = nota.trim()

                                // Llama tu endpoint de actualización/edición del carrito
                                // Ajusta parámetros según tu API:
                                /*viewModelGuardar.editarProductoCarritoRetrofit(
                                    idusuario = idusuario,
                                    idFilaCarrito = idFilaCarrito,
                                    cantidad = cantidadElegida,
                                    nota = notaElegida
                                )*/
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorLetraRoja),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.actualizar),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    item { Spacer(Modifier.height(15.dp)) }
                }
            }

            if (isLoading) LoadingModal(isLoading = true)

            /*if (showModal1Boton) {
                CustomModal1BotonTitulo(
                    show = showModal1Boton,
                    titulo = modalTituloString,
                    mensaje = modalMensajeString,
                    onDismiss = { showModal1Boton = false }
                )
            }*/
        }
    }

    // manejar resultado de GUARDAR
    /*resultadoGuardar?.getContentIfNotHandled()?.let { res ->
        // Ajusta según tu backend:
        if (res.success == 1 || res.success == 6) {
            CustomToasty(
                ctx,
                ctx.getString(R.string.actualizado_correctamente),
                ToastType.SUCCESS
            )
            navController.popBackStack()
        } else {
            modalTituloString = res.titulo ?: ctx.getString(R.string.atencion)
            modalMensajeString = res.mensaje ?: ctx.getString(R.string.error_reintentar_de_nuevo)
            showModal1Boton = true
        }
    }*/
}

/** ===== Utilidades ===== **/

private fun formatearUSD(monto: Double): String {
    return "$" + String.format(Locale.US, "%.2f", monto)
}