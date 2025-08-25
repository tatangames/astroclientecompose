package com.tatanstudios.astropollocliente.vistas.principal.productos


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.navOptions
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.CardHistorialOrden
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.model.modelos.ModeloDireccionesArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloHistorialOrdenesArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductos
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductosArray
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.viewmodel.HistorialFechasBuscarViewModel
import com.tatanstudios.astropollocliente.viewmodel.ListadoProductosViewModel
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TabRowDefaults.Divider
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
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductosTerceraArray
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.viewmodel.EnviarProductoAlCarritoViewModel
import com.tatanstudios.astropollocliente.viewmodel.InformacionProductoViewModel
import java.util.Locale

@Composable
fun ElegirProductoScreen(
    navController: NavHostController,
    idProducto: Int,
    viewModel: InformacionProductoViewModel = viewModel(),
    viewModelEnviar: EnviarProductoAlCarritoViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val tokenManager = remember { TokenManager(ctx) }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingEnviar by viewModelEnviar.isLoading.observeAsState(true)
    val resultadoEnviar by viewModelEnviar.resultado.observeAsState()


    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }
    var modalTituloString by remember { mutableStateOf("") }


    // estado UI
    var producto by remember { mutableStateOf<ModeloInformacionProductoArray?>(null) }
    var cantidad by remember { mutableStateOf(1) }
    var nota by remember { mutableStateOf("") }
    var errorNotaObligatoria by remember { mutableStateOf(false) }
    var idusuario by remember { mutableStateOf("") }

    // cargar datos
    LaunchedEffect(idProducto) {
        viewModel.informacionProductoRetrofit(idProducto)
    }

    // ocultar teclado al entrar
    LaunchedEffect(Unit) {
        idusuario = tokenManager.idUsuario.first()
        keyboardController?.hide()
    }

    // manejar resultado retrofit
    resultado?.getContentIfNotHandled()?.let { result ->
        if (result.success == 1 && result.informacionProducto.isNotEmpty()) {
            producto = result.informacionProducto.first() // asumo 1 ítem por id
        } else {
            CustomToasty(
                ctx,
                stringResource(id = R.string.error_reintentar_de_nuevo),
                ToastType.ERROR
            )
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.elegir_cantidad),
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
                // parsear precio unitario
                val precioUnit = prod.precio?.toDoubleOrNull() ?: 0.0
                val total = precioUnit * cantidad

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // IMAGEN (o placeholder)
                    item {
                        val tieneImagen = prod.utilizaImagen == 1 && !prod.imagen.isNullOrBlank()
                        if (tieneImagen) {
                            // Cárgala desde tu backend (reemplaza por tu URL base)
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

                    // STEPPER CANTIDAD
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
                                    containerColor = colorResource(R.color.colorLetraRoja),      // Fondo gris claro
                                    contentColor = Color.White             // Icono negro
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
                                    containerColor = colorResource(R.color.colorLetraRoja),      // Fondo gris claro
                                    contentColor = Color.White             // Icono negro
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Más")
                            }
                        }
                    }

                    // NOTAS (máx 300)
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
                                    focusedIndicatorColor = colorResource(R.color.colorAzul), // línea enfocada
                                    unfocusedIndicatorColor = Color.Gray,                     // línea sin foco
                                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                                    focusedContainerColor = Color.Transparent,                // sin fondo
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


                    // BOTÓN AGREGAR
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

                                viewModelEnviar.enviarProductoCarritoRetrofit(idusuario, idProducto, cantidadElegida, notaElegida)

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorLetraRoja), // 🔴 fondo del botón
                                contentColor = Color.White                         // ⚪ texto/icono
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.agregar_a_la_orden),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    item { Spacer(Modifier.height(15.dp)) }

                }
            }

            if (isLoading) LoadingModal(isLoading = true)
            if (isLoadingEnviar) LoadingModal(isLoading = true)


            if(showModal1Boton){
                CustomModal1BotonTitulo(showModal1Boton, modalTituloString, modalMensajeString, onDismiss = {showModal1Boton = false})
            }

        }
    }


    resultadoEnviar?.getContentIfNotHandled()?.let { result ->

        // REGLA 1: cliente no tiene direccion
        // REGLA 2: producto debe estar activo
        // REGLA 3: sub categoria del producto debe estar activo
        // REGLA 4: la categoria del producto debe estar activo
        // REGLA 5: la categoria si utiliza horario debe estar disponible

        if (result.success in 1..5) {
            modalTituloString = result.titulo ?: ""
            modalMensajeString = result.mensaje ?: ""
            showModal1Boton = true
        }
        else if(result.success == 6){
            CustomToasty(
                ctx,
                stringResource(id = R.string.agregado_al_carrito),
                ToastType.SUCCESS
            )
            navController.popBackStack()
        }else{
            CustomToasty(
                ctx,
                stringResource(id = R.string.error_reintentar_de_nuevo),
                ToastType.ERROR
            )
        }

    }
}

/** ===== Utilidades ===== **/

private fun formatearUSD(monto: Double): String {
    // Formato simple tipo $10.25
    return "$" + String.format(Locale.US, "%.2f", monto)
}