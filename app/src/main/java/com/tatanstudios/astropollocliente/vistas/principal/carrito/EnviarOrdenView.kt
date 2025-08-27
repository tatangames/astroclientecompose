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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomModal1BotonTitulo
import com.tatanstudios.astropollocliente.componentes.CustomModal2Botones
import com.tatanstudios.astropollocliente.model.modelos.ModeloInformacionProductoArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductosTerceraArray
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.viewmodel.EnviarOrdenFinalViewModel
import com.tatanstudios.astropollocliente.viewmodel.EnviarProductoAlCarritoViewModel
import com.tatanstudios.astropollocliente.viewmodel.InformacionOrdenParaEnviarViewModel
import com.tatanstudios.astropollocliente.viewmodel.InformacionProductoViewModel
import com.tatanstudios.astropollocliente.viewmodel.VerificarCuponViewModel
import com.tatanstudios.astropollocliente.vistas.login.getVersionName
import retrofit2.http.Field
import java.util.Locale


@Composable
fun EnviarOrdenScreen(
    navController: NavHostController,
    viewModel: InformacionOrdenParaEnviarViewModel = viewModel(),
    viewModelVerificarCupon: VerificarCuponViewModel = viewModel(),
    viewModelEnviarOrden: EnviarOrdenFinalViewModel = viewModel(),
) {
    val ctx = LocalContext.current

    val tokenManager = remember { TokenManager(ctx) }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingVerificarCupon by viewModelVerificarCupon.isLoading.observeAsState(true)
    val resultadoVerificarCupon by viewModelVerificarCupon.resultado.observeAsState()

    val isLoadingEnviarOrden by viewModelEnviarOrden.isLoading.observeAsState(true)
    val resultadoEnviarOrden by viewModelEnviarOrden.resultado.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current


    var idusuario by remember { mutableStateOf("") }

    // 1: SI PUEDE ORDENAR   0: NO PUEDE ORDENAR
    var minimoInt0NoPuede by remember { mutableStateOf(0) }
    var mensajeMinimoConsumoString by remember { mutableStateOf("") }
    var direccionString by remember { mutableStateOf("") }
    var clienteString by remember { mutableStateOf("") }
    var totalString by remember { mutableStateOf("") }
    var usaCuponPermitoServerInt by remember { mutableStateOf(0) }
    var usaPremioInt by remember { mutableStateOf(0) }
    var textoPremioString by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }

    // === Estado del modal de cupón ===
    var mostrarDialogCupon by remember { mutableStateOf(false) }

    var datosCargados by remember { mutableStateOf(false) }


    var textoCuponEscrito by remember { mutableStateOf("") }


    var tengoCupon by remember { mutableStateOf(0) }
    // MENSAJE DE LO QUE APLICA EL CUPON
    var textoCuponGanado by remember { mutableStateOf("") }

    // LA PRIMERA PALABRA (TOTAL) QUE CAMBIARA A SUB TOTAL CUANDO SE APLIQUE CUPON DINERO O DESCUENTO
    var txtSubTotalLetra by remember { mutableStateOf("Total") }

    val versionLocal = getVersionName(ctx)

    // PARA SABER SI ES CUPON DE DINERO O PORCENTAJE
    var tengoCuponDineroOrPorcentaje by remember { mutableStateOf(false) }

    // CUANDO SE APLICA CUPON DINERO O PORCENTAJE, SABER CUANTO SE PAGARA AL FINAL
    var totalCancelarPorCupon by remember { mutableStateOf("") }

    // PARA MOSTRAR MODAL PARA ENVIAR ORDEN SI OR NO
    var showModal2BotonParaEnviarOrden by remember { mutableStateOf(false) }

    // cargar datos
    LaunchedEffect(Unit) {
        idusuario = tokenManager.idUsuario.first()
        viewModel.informacionOrdenParaEnviarRetrofit(idusuario)
    }


    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(id = R.string.enviar_orden),
                colorResource(id = R.color.colorRojo)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (datosCargados) {

                // ===== CONTENIDO SCROLLEABLE/DE PANTALLA =====
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Faja Total
                    Surface(
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = txtSubTotalLetra,
                                color = Color.Black,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = totalString,
                                color = Color.Black,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))









                    // ===== Botón "Agregar cupón" a la derecha (solo si usaCupon == 1) =====
                    if (usaCuponPermitoServerInt == 1) {

                        val verde = Color(0xFF2E7D32)
                        val rojo  = Color(0xFFC62828)
                        val colorBoton = if (tengoCupon == 1) rojo else verde

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(Modifier.weight(1f))
                            Button(
                                onClick = {
                                    // tu lógica:
                                    // si no tiene cupón, abre el diálogo
                                    if (tengoCupon == 0) {
                                        textoCuponEscrito = ""
                                        mostrarDialogCupon = true
                                    } else {
                                        // si ya tiene cupón, por ejemplo lo “quita”
                                        tengoCupon = 0
                                        tengoCuponDineroOrPorcentaje = false
                                        textoCuponEscrito = ""
                                        txtSubTotalLetra = "Total"

                                        viewModel.informacionOrdenParaEnviarRetrofit(idusuario)
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorBoton,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = stringResource(
                                        if (tengoCupon == 1) R.string.borrar_cupon else R.string.agregar_cupon
                                    )
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                    }




                    Divider(color = Color.LightGray, thickness = 1.5.dp)

                    if(minimoInt0NoPuede == 0) {
                        // === CARD PARA MOSTRAR MINIMO DE CONSUMO ===
                        Spacer(Modifier.height(15.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E6EB)) // opcional
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = stringResource(id = R.string.minimo_de_consumo),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = mensajeMinimoConsumoString,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }



                    if(tengoCupon == 1) {
                        // === CARD PARA MOSTRAR DE QUE ES EL CUPON APLICADO ===
                        Spacer(Modifier.height(15.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E6EB)) // opcional
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = stringResource(id = R.string.cupon),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = textoCuponGanado,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp
                                )
                            }


                            if(tengoCuponDineroOrPorcentaje){
                                Column(modifier = Modifier.padding(14.dp)) {

                                    Text(
                                        text = stringResource(id = R.string.total_a_pagar),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = totalCancelarPorCupon,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                        }
                    }



                    if(usaPremioInt == 1) {
                        // === CARD PARA MOSTRAR SI VAMOS A CANJEAR UN PREMIO ===
                        Spacer(Modifier.height(15.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E6EB)) // opcional
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = stringResource(id = R.string.premio),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = textoPremioString,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp
                                )
                            }

                        }
                    }


                    Spacer(Modifier.height(15.dp))

                    // Card Dirección de entrega
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E6EB)) // opcional
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = stringResource(id = R.string.direccion_de_entrega),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = stringResource(id = R.string.cliente),
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = clienteString,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = stringResource(id = R.string.direccion),
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = direccionString,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Divider(color = Color.LightGray, thickness = 1.5.dp)

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.nota_para_la_orden_ejemplo),
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(6.dp))

                    TextField(
                        value = nota,
                        onValueChange = { nota = if (it.length <= 300) it else it.take(300) },
                        placeholder = { Text(stringResource(R.string.nota)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = colorResource(id = R.color.colorAzul),
                            unfocusedIndicatorColor = Color.Gray,
                            errorIndicatorColor = MaterialTheme.colorScheme.error,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        )
                    )

                    // ⚠️ No agregues aquí el botón: queda abajo del Box
                    Spacer(Modifier.height(96.dp)) // espacio para que el contenido no quede tapado por el botón fijo
                }


                // ===== Botón fijo, anclado al fondo =====
                Button(
                    onClick = {
                        // Ejemplo de validación:
                        if(minimoInt0NoPuede == 0){
                            CustomToasty(
                                ctx,
                                "Mínimo de Compra es Requerido",
                                ToastType.INFO
                            )
                        }else{
                            showModal2BotonParaEnviarOrden = true
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp) // margen visual
                        .navigationBarsPadding() // respeta la barra de gestos
                        .imePadding()            // evita teclado
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.colorRojo),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(stringResource(R.string.confirmar_orden).uppercase())
                }



                // ===== Modal de cupón =====
                if (mostrarDialogCupon) {
                    val botonVerificarHabilitado = textoCuponEscrito.trim().isNotEmpty()

                    AlertDialog(
                        onDismissRequest = { mostrarDialogCupon = false },
                        title = { Text("Agregar cupón") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = textoCuponEscrito,
                                    onValueChange = {
                                        val nuevo = if (it.length <= 100) it else it.take(100)
                                        textoCuponEscrito = nuevo
                                    },
                                    singleLine = true,
                                    placeholder = { Text("Escribe tu cupón") },
                                    modifier = Modifier.fillMaxWidth(),

                                    // ✅ Compatibilidad: usar TextFieldDefaults.colors()
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Black,   // borde/linea en foco
                                        unfocusedIndicatorColor = Color.Black, // borde/linea sin foco
                                        errorIndicatorColor = MaterialTheme.colorScheme.error,
                                        cursorColor = Color.Black,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent
                                    )
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        "${textoCuponEscrito.length}/100",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        },
                        // Verificar: verde con texto blanco (deshabilitado si vacío)
                        confirmButton = {
                            Button(
                                onClick = {
                                    val cup = textoCuponEscrito.trim()
                                    if (cup.isEmpty()) {

                                    } else {
                                        mostrarDialogCupon = false
                                        viewModelVerificarCupon.verificarCuponRetrofit(idusuario, textoCuponEscrito)
                                    }
                                },
                                enabled = botonVerificarHabilitado,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E7D32),
                                    contentColor = Color.White
                                )
                            ) { Text("Verificar") }
                        },
                        // Cancelar: negro con texto blanco
                        dismissButton = {
                            Button(
                                onClick = { mostrarDialogCupon = false },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White
                                )
                            ) { Text("Cancelar") }
                        }
                    )
                }

            } // -end if-datoscargados

            if(isLoading) LoadingModal(isLoading = isLoading)
            if(isLoadingVerificarCupon) LoadingModal(isLoading = isLoadingVerificarCupon)
            if(isLoadingEnviarOrden) LoadingModal(isLoading = isLoadingEnviarOrden)

            if(showModal2BotonParaEnviarOrden){
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.enviar_orden),
                    onDismiss = { showModal2BotonParaEnviarOrden = false },
                    onAccept = {
                        showModal2BotonParaEnviarOrden = false

                        viewModelEnviarOrden.enviarOrdenRetrofit(idusuario, nota, textoCuponEscrito, tengoCupon, versionLocal,
                           null)

                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

        }
    }




    // Manejo del resultado del ViewModel
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                // SIN DIRECCION EL CLIENTE
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
                navController.popBackStack()
            }
            2 -> {
                // Datos correctos
                minimoInt0NoPuede = result.minimo
                mensajeMinimoConsumoString = result.mensaje ?: ""
                totalString = result.total ?: ""
                direccionString = result.direccion ?: ""
                clienteString = result.cliente ?: ""
                usaCuponPermitoServerInt = result.usacupon

                // PREMIOS POR ACUMULAR PUNTOS
                usaPremioInt = result.usapremio
                textoPremioString = result.textopremio ?: ""
                datosCargados = true
            }
            3 -> {
                // CARRITO NO ENCONTRADO
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
                navController.popBackStack()
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
                navController.popBackStack()
            }
        }
    }


    resultadoVerificarCupon?.getContentIfNotHandled()?.let { result ->
        when (result.success) {

            // RETORNOS
            // 1: carrito de compras no encontrado
            // 1: cupon no valido (estado igual)

            // 2: cupon producto gratis
            // 3: cupon descuento de dinero
            // 4: cupon descuento de porcentaje

            1 -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.cupon_no_valido),
                    ToastType.INFO
                )
            }
            2 -> {
                // CUPON PARA PRODUCTO GRATIS
                textoCuponGanado = result.mensaje ?: ""
                tengoCupon = 1
            }
            3 -> {
                // CUPON PARA DESCUENTO DE DINERO
                textoCuponGanado = result.mensaje ?: ""
                // aplico
                // resta

                totalCancelarPorCupon = result.resta ?: ""

                txtSubTotalLetra = "Sub Total"
                tengoCupon = 1
                tengoCuponDineroOrPorcentaje = true
            }
            4 -> {
                // CUPON PARA PORCENTAJE DINERO
                textoCuponGanado = result.mensaje ?: ""
                // aplico
                // resta

                totalCancelarPorCupon = result.resta ?: ""

                txtSubTotalLetra = "Sub Total"
                tengoCupon = 1
                tengoCuponDineroOrPorcentaje = true
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
                navController.popBackStack()
            }
        }
    }




    resultadoEnviarOrden?.getContentIfNotHandled()?.let { result ->
        when (result.success) {

            // REGLAS
            1 -> {
               // titulo y mensaje
            }
            2 -> {
                // ORDEN ENVIADA
                // ENVIAR NOTIFICACION A RESTAURANTE
                //peticionNotiRestaurante(apiRespuesta.getIdorden());

                //ordenEnviada(apiRespuesta.getTitulo(), apiRespuesta.getMensaje());
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
                navController.popBackStack()
            }
        }
    }



}



