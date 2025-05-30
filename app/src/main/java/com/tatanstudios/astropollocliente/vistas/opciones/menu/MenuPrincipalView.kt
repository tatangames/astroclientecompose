package com.tatanstudios.astropollocliente.vistas.opciones.menu

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.SolicitarPermisosUbicacion
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.modelos.ModeloDireccionesArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloMenuPrincipalCategoriasArray
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.ui.theme.ColorBlanco
import com.tatanstudios.astropollocliente.ui.theme.ColorGris
import com.tatanstudios.astropollocliente.viewmodel.ListadoMenuPrincipal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuPrincipalScreen(navController: NavHostController,
                        viewModel: ListadoMenuPrincipal = viewModel()
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // listado de productos
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    //val keyboardController = LocalSoftwareKeyboardController.current


    var boolDatosCargados by remember { mutableStateOf(false) }
    var popPermisoGPS by remember { mutableStateOf(false) }



    var imageUrls by remember { mutableStateOf(listOf<String>()) }

    var modeloListaCategoriasArray by remember { mutableStateOf(listOf<ModeloMenuPrincipalCategoriasArray>()) }



    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }

    var showModal1BotonUsuarioBloqueado by remember { mutableStateOf(false) }


    // Lanzar la solicitud cuando se carga la pantalla
    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
            viewModel.listadoMenuPrincipalRetrofit(idusuario)
        }
    }

    // CUANDO HAYA CARGADO LA VISTA, VERIFICAR SI HAY PERMISOS UBICACION
    if(boolDatosCargados){
        SolicitarPermisosUbicacion (
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
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorResource(id = R.color.colorCremaV1))
        ) {
            // 1. HorizontalPager con imágenes
            if (imageUrls.isNotEmpty()) {
                item {
                    val pagerState = rememberPagerState(pageCount = { imageUrls.size })
                    Column {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                        ) { page ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
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
                        }

                        // Indicadores de página
                        Row(
                            Modifier
                                .height(50.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color =
                                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Texto vertical con título y "ver más"
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,          // Color rojo directo
                            fontSize = 20.sp            // Tamaño de fuente personalizado
                        )
                    )
                    Text(
                        text = "Ver más",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,        // Color negro directo
                            fontSize = 16.sp            // Tamaño personalizado para este texto
                        ),
                        modifier = Modifier.clickable { /* acción */ }
                    )
                }
            }


            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 3. Grid horizontal con 2 filas para categorías
            item {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    modifier = Modifier
                        .height(380.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(modeloListaCategoriasArray) { categoria ->
                        val imagenUrl = "${RetrofitBuilder.urlImagenes}${categoria.imagen}"
                        Card(
                            modifier = Modifier
                                .width(140.dp)
                                .height(180.dp)
                                .clickable { /* acción */ },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(imagenUrl)
                                        .crossfade(true)
                                        .placeholder(R.drawable.spinloading)
                                        .error(R.drawable.camaradefecto)
                                        .build(),
                                    contentDescription = categoria.nombre,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .height(100.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(Modifier.height(8.dp))
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















        if (isLoading) {
            LoadingModal(isLoading = true)
        }


        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // CLIENTE BLOQUEADO
                    showModal1BotonUsuarioBloqueado = true
                }
                2 -> {
                    // NO HAY DIRECCION DE ENTREGA
                    scope.launch {
                        navigateToDirecciones(navController)
                    }
                }
                3 -> {
                    // MENU DE PRODUCTOS
                    imageUrls = result.arraySlider.map { sliderItem ->
                        // Construir la URL completa de la imagen
                        "${RetrofitBuilder.urlImagenes}${sliderItem.imagen}"
                    }

                    modeloListaCategoriasArray = result.arrayCategorias

                    boolDatosCargados = true
                }
                4 -> {
                    // NO HAY UN SERVICIO ASOCIADO A LA ZONA
                    modalMensajeString = result.mensaje ?: ""
                    showModal1Boton = true
                }
                5 -> {
                    // NO HAY DIRECCION DE ENTREGA SELECCIONADA
                    scope.launch {
                        navigateToDirecciones(navController)
                    }
                }
                else -> {
                    // Error, recargar de nuevo
                    CustomToasty(
                        ctx,
                        stringResource(id = R.string.error_reintentar_de_nuevo),
                        ToastType.ERROR
                    )
                }
            }
        }


        if(showModal1Boton){
            CustomModal1Boton(showModal1Boton, modalMensajeString, onDismiss = {showModal1Boton = false})
        }

        if(showModal1BotonUsuarioBloqueado){
            CustomModal1Boton(showModal1BotonUsuarioBloqueado, stringResource(R.string.usuario_bloqueado), onDismiss = {


                scope.launch {
                    // Llamamos a deletePreferences de manera segura dentro de una coroutine
                    tokenManager.deletePreferences()

                    // cerrar modal
                    showModal1BotonUsuarioBloqueado = false

                    navigateToLogin(navController)
                }
            })
        }



        if(popPermisoGPS){
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
                    ) {
                        Text(stringResource(R.string.ir_a_ajustes))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            popPermisoGPS = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorGris,
                            contentColor = ColorBlanco
                        )
                    ) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            )
        }

    } // end-scalfold
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


private fun navigateToDirecciones(navController: NavHostController) {
    navController.navigate(Routes.VistaMisDirecciones.route) {
        popUpTo(Routes.VistaMisDirecciones.route) {
            inclusive = true // Elimina VistaPrincipal de la pila
        }
        launchSingleTop = true // Asegura que no se creen múltiples instancias de VistaLogin
    }
}
