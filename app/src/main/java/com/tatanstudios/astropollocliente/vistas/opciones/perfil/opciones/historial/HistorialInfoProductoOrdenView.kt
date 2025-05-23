package com.tatanstudios.astropollocliente.vistas.opciones.perfil.opciones.historial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.viewmodel.InfoProductoViewModel
import kotlinx.coroutines.launch


@Composable
fun InfoProductoHistorialScreen(navController: NavHostController, _idproducto: Int,
                       viewModel: InfoProductoViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var imagenProducto by remember { mutableStateOf("") }
    var boolDatosCargados by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()


    var _titulo by remember { mutableStateOf("") }
    var _descripcion by remember { mutableStateOf("") }
    var _precio by remember { mutableStateOf("") }
    var _unidades by remember { mutableStateOf("") }
    var _total by remember { mutableStateOf("") }
    var _notas by remember { mutableStateOf("") }
    var boolUsaImagen by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.infoProductoRetrofit(idproducto = _idproducto)
        }
    }


    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.producto),
                colorResource(R.color.colorRojo)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (boolDatosCargados) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {

                            if(boolUsaImagen){
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("${RetrofitBuilder.urlImagenes}${imagenProducto}")
                                        .crossfade(true)
                                        .placeholder(R.drawable.camaradefecto)
                                        .error(R.drawable.camaradefecto)
                                        .build(),
                                    contentDescription = stringResource(R.string.imagen_por_defecto),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }


                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = _titulo,
                                color = Color.Red,
                                fontSize = 24.sp,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if(_descripcion.isNotBlank()){
                                Text(
                                    text = _descripcion,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }


                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.precio) + ": ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                        append(_precio)
                                    }
                                },
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.unidades) + ": ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                        append(_unidades)
                                    }
                                },
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.total) + ": ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                        append(_total)
                                    }
                                },
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.nota) + ": ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                        append(_notas)
                                    }
                                },
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp
                            )
                        }
                    }
                }


            }
        }



        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // INFO PRODUCTO
                    for (item in result.lista) {
                        _titulo = item.nombreProducto?: ""
                        _descripcion = item.descripcion?: ""
                        _precio = item.precio?: ""
                        _unidades = item.cantidad.toString()
                        _total = item.multiplicado?: ""
                        _notas = item.nota?: ""

                        if(item.utilizaImagen == 1){
                            boolUsaImagen = true
                        }
                    }


                    boolDatosCargados = true

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


        if (isLoading) {
            LoadingModal(isLoading = true)
        }

    } // end-scalfold






}