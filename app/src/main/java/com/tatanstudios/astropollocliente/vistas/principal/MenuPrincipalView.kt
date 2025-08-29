package com.tatanstudios.astropollocliente.vistas.opciones.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.SolicitarPermisosUbicacion
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.modelos.ModeloMenuPrincipalCategoriasArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloMenuPrincipalPopularesArray
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.ui.theme.ColorBlanco
import com.tatanstudios.astropollocliente.ui.theme.ColorGris
import com.tatanstudios.astropollocliente.viewmodel.ListadoMenuPrincipal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuPrincipalScreen(
    navController: NavHostController,
    viewModel: ListadoMenuPrincipal = viewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val layoutDirection = LocalLayoutDirection.current

    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    var boolDatosCargados by remember { mutableStateOf(false) }
    var popPermisoGPS by remember { mutableStateOf(false) }

    var imageUrls by remember { mutableStateOf(listOf<String>()) }
    var modeloListaCategoriasArray by remember { mutableStateOf(listOf<ModeloMenuPrincipalCategoriasArray>()) }
    var modeloListaPopularesArray by remember { mutableStateOf(listOf<ModeloMenuPrincipalPopularesArray>()) }

    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }
    var showModal1BotonUsuarioBloqueado by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        idusuario = tokenManager.idUsuario.first()
        viewModel.listadoMenuPrincipalRetrofit(idusuario)
    }

    if (boolDatosCargados) {
        SolicitarPermisosUbicacion(
            onPermisosConcedidos = { },
            onPermisosDenegados = { }
        )
    }

    Scaffold(
        topBar = {
            BarraToolbarColorMenuPrincipal(
                navController,
                stringResource(R.string.menu),
                colorResource(R.color.colorRojo)
            )
        },
        // 👇 evita que este Scaffold agregue insets extra que se sumen al padre
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->

        // 👇 combinamos el top padding del topBar local con el padding del padre (FAB/bottom bar)
        val totalPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding(),
            start = contentPadding.calculateStartPadding(layoutDirection),
            end = contentPadding.calculateEndPadding(layoutDirection)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorCremaV1)),
            contentPadding = totalPadding
        ) {
            // 1) Slider
            if (imageUrls.isNotEmpty()) {
                item {
                    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

                    Column {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .height(210.dp)
                        ) { page ->
                            AsyncImage(
                                model = ImageRequest.Builder(ctx)
                                    .data(imageUrls[page])
                                    .crossfade(true)
                                    .placeholder(R.drawable.spinloading)
                                    .error(R.drawable.camaradefecto)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        // indicadores
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(50.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(pagerState.pageCount) { i ->
                                val dotColor =
                                    if (pagerState.currentPage == i) Color.DarkGray else Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(MaterialTheme.shapes.extraLarge)
                                        .background(dotColor)
                                        .size(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (!modeloListaCategoriasArray.isNullOrEmpty()) {

                // 2) Título unicamente
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Categorías",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Red,
                                fontSize = 20.sp
                            )
                        )
                    }
                }


                // 3) Grid categorías
                item {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(2),
                        modifier = Modifier
                            // alto = 2 filas * altoCard + 1 spacing entre filas + padding vertical
                            .height(170.dp * 2 + 8.dp + 8.dp + 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp
                        )
                    ) {
                        items(modeloListaCategoriasArray) { categoria ->
                            val imagenUrl = "${RetrofitBuilder.urlImagenes}${categoria.imagen}"

                            Card(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(170.dp) // 🔹 altura fija para alinear filas
                                    .clickable {

                                        navController.navigate(
                                            Routes.VistaListadoProductos.createRoute(categoria.id)
                                        ) {
                                            popUpTo(Routes.VistaListadoProductos.route) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }

                                    },
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(ctx)
                                            .data(imagenUrl)
                                            .crossfade(true)
                                            .placeholder(R.drawable.spinloading)
                                            .error(R.drawable.camaradefecto)
                                            .build(),
                                        contentDescription = categoria.nombre,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .height(90.dp)
                                            .fillMaxWidth()
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = categoria.nombre ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

            }

            if (!modeloListaPopularesArray.isNullOrEmpty()) {

                item { Spacer(Modifier.height(10.dp)) }

                // 2) Título unicamente
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Populares",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Red,
                                fontSize = 20.sp
                            )
                        )
                    }
                }


                /// AQUI VA POPULARES


                item {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp) // 🔹 separa el 1er y último card
                    ) {
                        items(modeloListaPopularesArray) { prod ->

                            if(prod.utilizaImagen == 1)
                            {
                                val imagenUrl = "${RetrofitBuilder.urlImagenes}${prod.imagen}"
                                Card(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .wrapContentHeight() // 🔹 evita espacio vacío
                                    .clickable {

                                        navController.navigate(
                                            Routes.VistaInformacionProducto.createRoute(prod.id)
                                        ) {
                                            popUpTo(Routes.VistaInformacionProducto.route) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }

                                     },
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp) // 🔹 control fino de separaciones
                                    ) {
                                        // Imagen
                                        AsyncImage(
                                            model = ImageRequest.Builder(ctx)
                                                .data(imagenUrl)
                                                .crossfade(true)
                                                .placeholder(R.drawable.spinloading)
                                                .error(R.drawable.camaradefecto)
                                                .build(),
                                            contentDescription = prod.nombre,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .height(84.dp)           // 🔹 un poco más compacta
                                                .fillMaxWidth()
                                        )

                                        // Nombre
                                        Text(
                                            text = prod.nombre ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        // Precio centrado + botón en misma fila
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = prod.precio?.let { "$it" } ?: "$0.00",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF444444),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.weight(1f) // centra el precio
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .size(26.dp) // 🔹 un poco más compacto
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFE74C3C))
                                                    .clickable { /* onAdd(prod) */ },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Agregar",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                            }else{
                                Image(
                                    painter = painterResource(id = R.drawable.camaradefecto),
                                    contentDescription = prod.nombre,
                                    modifier = Modifier
                                        .size(72.dp)                 // 👈 define tamaño cuadrado
                                        .clip(CircleShape)           // 👈 recorta en forma de círculo
                                        .border(2.dp, Color.LightGray, CircleShape), // 👈 borde opcional
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }

        // ======= tus diálogos/modales abajo tal cual =======
        if (isLoading) LoadingModal(true)

        LaunchedEffect(resultado) {
            resultado?.getContentIfNotHandled()?.let { result ->
                when (result.success) {
                    1 -> showModal1BotonUsuarioBloqueado = true
                    2 -> navigateToDirecciones(navController, btnBloqueoAtras = 1)
                    3 -> {
                        imageUrls = result.arraySlider.map { "${RetrofitBuilder.urlImagenes}${it.imagen}" }
                        modeloListaCategoriasArray = result.arrayCategorias
                        modeloListaPopularesArray = result.arrayPopulares
                        boolDatosCargados = true
                    }
                    4 -> { modalMensajeString = result.mensaje ?: ""; showModal1Boton = true }
                    5 -> navigateToDirecciones(navController, btnBloqueoAtras = 1)
                    else -> CustomToasty(ctx, ctx.getString(R.string.error_reintentar_de_nuevo), ToastType.ERROR)
                }
            }
        }

        if (showModal1Boton) {
            CustomModal1Boton(showModal1Boton, modalMensajeString) { showModal1Boton = false }
        }

        if (showModal1BotonUsuarioBloqueado) {
            CustomModal1Boton(showModal1BotonUsuarioBloqueado, ctx.getString(R.string.usuario_bloqueado)) {
                scope.launch {
                    tokenManager.deletePreferences()
                    showModal1BotonUsuarioBloqueado = false
                    navigateToLogin(navController)
                }
            }
        }

        if (popPermisoGPS) {
            AlertDialog(
                onDismissRequest = { popPermisoGPS = false },
                title = { Text(stringResource(R.string.permiso_gps_requerido)) },
                text = { Text(stringResource(R.string.para_usar_esta_funcion_gps)) },
                confirmButton = {
                    Button(
                        onClick = {
                            popPermisoGPS = false
                            redireccionarAjustes(ctx)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.colorAzul),
                            contentColor = colorResource(R.color.colorBlanco)
                        )
                    ) { Text(stringResource(R.string.ir_a_ajustes)) }
                },
                dismissButton = {
                    Button(
                        onClick = { popPermisoGPS = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorGris,
                            contentColor = ColorBlanco
                        )
                    ) { Text(stringResource(R.string.cancelar)) }
                }
            )
        }
    } // end Scaffold
}


// REDIRECCIONAR
fun redireccionarAjustes(context: Context){
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}


private fun navigateToLogin(navController: NavHostController) {
    navController.navigate(Routes.VistaLogin.route) {
        popUpTo(Routes.VistaPrincipal.route) {
            inclusive = true // Elimina VistaPrincipal de la pila
        }
        launchSingleTop = true // Asegura que no se creen múltiples instancias de VistaLogin
    }
}


private fun navigateToDirecciones(
    navController: NavHostController,
    btnBloqueoAtras: Int = 0
) {

    navController.navigate(
        Routes.VistaMisDirecciones.createRoute(btnBloqueoAtras)
    ) {
        popUpTo(Routes.VistaMisDirecciones.route) {
            inclusive = true
        }
        launchSingleTop = true
    }
}


