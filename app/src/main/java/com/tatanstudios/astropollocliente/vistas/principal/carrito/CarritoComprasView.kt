package com.tatanstudios.astropollocliente.vistas.principal.carrito

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorCarritoCompras
import com.tatanstudios.astropollocliente.componentes.CustomModal2Botones
import com.tatanstudios.astropollocliente.model.modelos.ModeloCarritoTemporal
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.viewmodel.BorrarCarritoComprasViewModel
import com.tatanstudios.astropollocliente.viewmodel.BorrarFilaCarritoViewModel
import com.tatanstudios.astropollocliente.viewmodel.ListadoCarritoComprasViewModel
import kotlinx.coroutines.launch

@Composable
fun CarritoComprasScreen(
    navController: NavHostController,
    viewModel: ListadoCarritoComprasViewModel = viewModel(),
    viewModelBorrarCarrito: BorrarCarritoComprasViewModel = viewModel(),
    viewModelBorrarFila: BorrarFilaCarritoViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingBorrar by viewModelBorrarCarrito.isLoading.observeAsState(true)
    val resultadoBorrar by viewModelBorrarCarrito.resultado.observeAsState()

    val isLoadingFilaBorrar by viewModelBorrarFila.isLoading.observeAsState(true)
    val resultadoFilaBorrar by viewModelBorrarFila.resultado.observeAsState()


    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine

    var idusuario by remember { mutableStateOf("") }
    var datosCargados by remember { mutableStateOf(false) }   // usa solo este
    var estadoProductoGlobal by remember { mutableStateOf(false) }

    var subtotal: String by remember { mutableStateOf("") }
    var modeloListaCarritoArray by remember {                 // usa esta lista
        mutableStateOf(listOf<ModeloCarritoTemporal>())
    }

    // MODAL PREGUNTA BOTON
    var showModal2Boton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        idusuario = TokenManager(ctx).idUsuario.first()
        viewModel.listadoCarritoComprasRetrofit(idusuario)
    }

    keyboardController?.hide()

    Scaffold(
        topBar = {
            // Pasa argumentos posicionales (o usa los nombres correctos según tu función)
            BarraToolbarColorCarritoCompras(
                navController,
                stringResource(R.string.carrito),
                colorResource(R.color.colorRojo),
                onDeleteClick = {
                    // 👇 aquí escuchas el click
                    if(datosCargados){
                        if(estadoProductoGlobal){
                            showModal2Boton = true
                        }else{
                            CustomToasty(
                                ctx,
                                "No hay Productos",
                                ToastType.INFO
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (datosCargados) BarraSubtotal(
                subtotal = subtotal,
                onClick = { /* navegar a checkout */ }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (datosCargados) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = modeloListaCarritoArray,
                        key = { it.carritoid }
                    ) { p ->
                        ItemCarritoSwipeAction(
                            p = p,
                            onClick = { item ->
                                navController.navigate(
                                    Routes.VistaEditarProducto.createRoute(item.carritoid)
                                ) {
                                    popUpTo(Routes.VistaInformacionProducto.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            onSwipeAction = { item ->


                                // 👉 AQUÍ MANDAR A ELIMINAR FILA
                                scope.launch {
                                    viewModelBorrarFila.eliminarFilaCarritoRetrofit(idusuario, item.carritoid)
                                }


                            }
                        )

                        Spacer(Modifier.height(6.dp))
                    }
                }
            }

            if (isLoading) LoadingModal(true)
            if(isLoadingBorrar) LoadingModal(true)
            if(isLoadingFilaBorrar) LoadingModal(true)

            // MENSAJES
            if(showModal2Boton){
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.borrar_carrito),
                    onDismiss = { showModal2Boton = false },
                    onAccept = {
                        showModal2Boton = false

                        // BORRAR CARRITO DE COMPRAS
                        scope.launch {
                            viewModelBorrarCarrito.eliminarCarritoComprasRetrofit(idusuario)
                        }

                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }
        }
    }


    // Manejo del resultado
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                // SI HAY DATOS

                modeloListaCarritoArray = result.listadoCarritoTemporal

                // producto no disponible
                estadoProductoGlobal = (result.estadoProductoGlobal == 1)

                subtotal = result.subTotal ?: ""
                datosCargados = true
            }
            2 -> {
                // NO HAY DATOS
                CustomToasty(
                    ctx,
                    stringResource(R.string.carrito_vacio),
                    ToastType.INFO
                )
                datosCargados = true
                subtotal = "$0.00"
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

    resultadoBorrar?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                // carrito borrado
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.carrito_borrado),
                    ToastType.SUCCESS
                )
                navController.popBackStack()
            }
            2 -> {
                // carrito de compras no encontrado
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.carrito_borrado),
                    ToastType.SUCCESS
                )
                navController.popBackStack()
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


    resultadoFilaBorrar?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                // carrito de compras borrado completamente
                datosCargados = false
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.carrito_borrado),
                    ToastType.SUCCESS
                )
                navController.popBackStack()
            }
            2 -> {
                // producto eliminado
                datosCargados = false
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.producto_eliminado),
                    ToastType.SUCCESS
                )
                scope.launch {
                    viewModel.listadoCarritoComprasRetrofit(idusuario)
                }
            }
            3 -> { // producto no encontrado

                datosCargados = false
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.producto_eliminado),
                    ToastType.SUCCESS
                )
                scope.launch {
                    viewModel.listadoCarritoComprasRetrofit(idusuario)
                }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemCarritoSwipeAction(
    p: ModeloCarritoTemporal,
    onClick: (ModeloCarritoTemporal) -> Unit,          // tap normal al card
    onSwipeAction: (ModeloCarritoTemporal) -> Unit     // acción al deslizar completo o tocar botón
) {
    val scope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                // Llamá tu función
                onSwipeAction(p)
                // NO eliminar: reseteamos para que el item quede visible

                false // <- indicamos que NO se “dismissee”
            } else false
        },
        positionalThreshold = { distance -> distance * 0.5f } // 50%
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFE5E5))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // Botón visible mientras deslizás (también dispara la acción)
                AssistChip(
                    onClick = {
                        onSwipeAction(p)
                        scope.launch { dismissState.reset() }
                    },
                    label = { Text("Borrar", color = Color.White) },
                    leadingIcon = {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFFFF1744)
                    )
                )
            }
        },
        content = {
            ItemCarritoCard(p = p, onClick = onClick)
        }
    )
}



@Composable
private fun ItemCarritoCard(p: ModeloCarritoTemporal,
                            onClick: (ModeloCarritoTemporal) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(p) }, // 👈 aquí detectás el click
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cantidad: Badge azul
            CantidadBadge(cantidad = p.cantidad)

            Spacer(Modifier.width(8.dp))

            // Imagen (opcional)
            if (p.utilizaImagen == 1 && !p.imagen.isNullOrBlank()) {

                val imagenUrl = "${RetrofitBuilder.urlImagenes}${p.imagen}"

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagenUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    placeholder = painterResource(R.drawable.spinloading),
                    error = painterResource(R.drawable.camaradefecto),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.camaradefecto),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .size(72.dp)                 // 👈 define tamaño cuadrado
                        .clip(CircleShape)           // 👈 recorta en forma de círculo
                        .border(2.dp, Color.LightGray, CircleShape), // 👈 borde opcional
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
            }

            // Nombre + Nota (columna central)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = p.nombre ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!p.titulo.isNullOrBlank() || !p.mensaje.isNullOrBlank()) {
                    Text(
                        text = (p.titulo.orEmpty() + " " + p.mensaje.orEmpty()).trim(),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.colorRojo),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }



            Text(
                text = p.precioformat?:"",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.widthIn(min = 72.dp)
            )
        }
        Divider(thickness = 1.dp, color = Color(0xFFE0E0E0))
    }
}

/* ---------- Badge cantidad ---------- */

@Composable
private fun CantidadBadge(cantidad: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(24.dp)
            .widthIn(min = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF1976D2)) // azul
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = "${cantidad}x",
            color = Color.White,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}

/* ---------- Bottom bar Subtotal ---------- */

@Composable
private fun BarraSubtotal(subtotal: String, onClick: () -> Unit) {
    Surface(shadowElevation = 6.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.colorRojo))
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.subtotal) + ": $subtotal",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}



/* ---------- Util ---------- */


