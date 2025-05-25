package com.tatanstudios.astropollocliente.vistas.principal.opciones.direcciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.CardMisDirecciones
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.modelos.ModeloDireccionesArray
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.viewmodel.ListadoDireccionesViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun MisDireccionesScreen(navController: NavHostController,
                                viewModel: ListadoDireccionesViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // listado de productos
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()
    var boolDatosCargados by remember { mutableStateOf(false) }
    var boolNohayDirecciones by remember { mutableStateOf(false) }

    var modeloListaDireccionesArray by remember { mutableStateOf(listOf<ModeloDireccionesArray>()) }

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    //val keyboardController = LocalSoftwareKeyboardController.current

    // Lanzar la solicitud cuando se carga la pantalla
    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
            viewModel.listadoDireccionesRetrofit(idusuario)
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.mis_direcciones),
                colorResource(R.color.colorRojo)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Acción al hacer clic, por ejemplo navegar a agregar dirección
                    navController.navigate(Routes.VistaMapa.route) {
                        popUpTo(Routes.VistaMapa.route) { inclusive = true }
                    }
                },
                containerColor = colorResource(R.color.colorRojo),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar dirección"
                )
            }
        }
    ) { innerPadding ->

        if (modeloListaDireccionesArray.isEmpty() && boolDatosCargados) {
            // MOSTRARA CUANDO NO HAYA DIRECCIONES

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.map),
                        contentDescription = stringResource(R.string.mis_direcciones),
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.no_hay_direccion_registrada),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
            }
        }else{

            // MOSTRAR CUANDO HAYA DIRECCIONES
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Listado dinámico
                items(modeloListaDireccionesArray) { tipoDato ->
                    // Aquí colocas tu componente personalizado o vista para cada producto

                    CardMisDirecciones(
                        nombre = tipoDato.nombre?: "",
                        seleccionado = tipoDato.seleccionado,
                        minimoCompra = tipoDato.minimocompra?: "",
                        direccion = tipoDato.direccion?: ""
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (isLoading) {
            LoadingModal(isLoading = true)
        }

    } // end-scalfold


    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                // NO HAY DIRECCION REGISTRADA
                boolNohayDirecciones = true
                boolDatosCargados = true
            }
            2 -> {
                // SI HAY DIRECCIONES
                boolDatosCargados = true
                boolNohayDirecciones = false
                modeloListaDireccionesArray = result.lista
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


}








