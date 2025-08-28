package com.tatanstudios.astropollocliente.vistas.principal.ordenes

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorCarritoCompras
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorParaListaOrdenes
import com.tatanstudios.astropollocliente.componentes.CustomModal2Botones
import com.tatanstudios.astropollocliente.model.modelos.ModeloCarritoTemporal
import com.tatanstudios.astropollocliente.model.modelos.ModeloOrdenesArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductosArray
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.viewmodel.BorrarCarritoComprasViewModel
import com.tatanstudios.astropollocliente.viewmodel.BorrarFilaCarritoViewModel
import com.tatanstudios.astropollocliente.viewmodel.ListadoCarritoComprasViewModel
import com.tatanstudios.astropollocliente.viewmodel.ListadoOrdenesViewModel
import kotlinx.coroutines.launch

@Composable
fun ListadoOrdenesScreen(
    navController: NavHostController,
    viewModel: ListadoOrdenesViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine

    var idusuario by remember { mutableStateOf("") }
    var datosCargados by remember { mutableStateOf(false) } // usa solo este


    var modeloOrdenesArray: List<ModeloOrdenesArray> by remember { mutableStateOf(listOf<ModeloOrdenesArray>()) }




    LaunchedEffect(Unit) {
        idusuario = TokenManager(ctx).idUsuario.first()
        viewModel.listadoOrdenesRetrofit(idusuario)
    }

    keyboardController?.hide()

    Scaffold(
        topBar = {
            // Pasa argumentos posicionales (o usa los nombres correctos según tu función)
            BarraToolbarColorParaListaOrdenes(
                stringResource(R.string.ordenes),
                colorResource(R.color.colorRojo),
            )
        },
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (datosCargados) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + 72.dp // 👈 espacio extra para BottomBar + FAB
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = modeloOrdenesArray,
                        key = { it.id }
                    ) { orden ->
                        // Decide apariencia/acción del botón según cancelada
                        val esCancelada = orden.estadoCancelada == 1
                        val textoBoton = if (esCancelada) "Borrar orden" else "Ver orden"
                        val coloresBoton = if (esCancelada) {
                            ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorLetraRoja), // rojo
                                contentColor = Color.White
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorVerde), // rojo
                                contentColor = Color.White
                            )
                        }

                        // Acción común (card y botón hacen lo mismo)
                        val onAccion = {
                            if (esCancelada) {
                                // TODO: borrar (ajusta a tu ViewModel)




                            } else {
                                // TODO: navegar a detalle

                                navController.navigate(
                                    Routes.VistaEstadoOrden.createRoute(orden.id)
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onAccion() },
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Encabezado: #orden + estado opcional

                                Text(
                                    text = "#Orden: ${orden.id}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )

                                // Fecha
                                Text(
                                    text = "Fecha: ${orden.fechaOrden ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )

                                Text(
                                    text = "Total: " + orden.totalFormat,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp,
                                    color = Color.DarkGray
                                )

                                // Dirección
                                Text(
                                    text = "Dirección: ${orden.direccion ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )


                                // === MOSTRAR SI TRAE PREMIO ===
                                if (orden.hayPremio == 1) {
                                    Text(
                                        text = "Precio: " + orden.textoPremio,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black, // morado para destacar
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // === MOSTRAR SI PUSO NOTA CLIENTE ORDEN ===
                                if (!orden.notaOrden.isNullOrBlank()) {
                                    Text(
                                        text = "Nota: " + orden.notaOrden,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black, // morado para destacar
                                        fontWeight = FontWeight.Medium
                                    )
                                }


                                // === MOSTRAR SI HAY CUPON ===
                                if (orden.hayCupon == 1) {
                                    if(!orden.textoPremio.isNullOrBlank()){
                                        Text(
                                            text = "Cupón: " + orden.textoPremio,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Black, // morado para destacar
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }


                                // === MOSTRAR SI FUE CANCELADA ===
                                if (orden.estadoCancelada == 1) {
                                    if(!orden.notaCancelada.isNullOrBlank()){
                                        Text(
                                            text = "Cancelada: " + orden.textoPremio,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Red,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                // Estado de la orden
                                Text(
                                    text = "Estado: " + orden.estado,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )



                                // Botón alineado a la derecha
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { onAccion() },
                                        colors = coloresBoton,
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(text = textoBoton)
                                    }
                                }
                            }
                        }
                    }
                }



            }

            if (isLoading) LoadingModal(true)

        }
    }


    // Manejo del resultado
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                // SI HAY DATOS
                datosCargados = true
                modeloOrdenesArray = result.ordenes
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
            }
        }
    }



}

