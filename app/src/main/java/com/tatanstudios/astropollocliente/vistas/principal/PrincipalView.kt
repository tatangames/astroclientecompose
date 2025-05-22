package com.tatanstudios.astropollocliente.vistas.principal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tatanstudios.astropollocliente.R
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.vistas.opciones.menu.MenuPrincipalScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PrincipalScreen(navController: NavHostController) {
    var selectedScreen by remember { mutableStateOf("menu") }
    val scope = rememberCoroutineScope()
    var canClickCart by remember { mutableStateOf(true) }

    // Declarar la función dentro del cuerpo composable
    val openCart = {
        if (canClickCart) {
            canClickCart = false
            navController.navigate(Routes.VistaCarrito.route) {
                launchSingleTop = true
            }
            scope.launch {
                delay(500) // Tiempo para prevenir doble click
                canClickCart = true
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBarWithCart(
                selectedScreen = selectedScreen,
                onMenuClick = { selectedScreen = "menu" },
                onOrdersClick = { selectedScreen = "ordenes" },
                onCartClick = openCart // ✅ sin error
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = openCart, // ✅ sin error
                backgroundColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    tint = Color(0xFF448AFF)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedScreen) {
                "menu" -> MenuPrincipalScreen(navController)
                "ordenes" -> PantallaOrdenes()
            }
        }
    }
}




@Composable
fun BottomAppBarWithCart(
    selectedScreen: String,
    onMenuClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onCartClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier.navigationBarsPadding(),
        cutoutShape = CircleShape,
        backgroundColor = Color.White,
        elevation = 8.dp
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onMenuClick() }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isSelected = selectedScreen == "menu"
                Icon(
                    painter = painterResource(id = R.drawable.icono_comida),
                    contentDescription = stringResource(R.string.menu),
                    tint = if (isSelected) Color.Red else Color.Gray
                )
                Text(
                    text = stringResource(R.string.menu),
                    color = if (isSelected) Color.Red else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Espacio para FAB

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOrdersClick() }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isSelected = selectedScreen == "ordenes"
                Icon(
                    painter = painterResource(id = R.drawable.icono_comida),
                    contentDescription = stringResource(R.string.ordenes),
                    tint = if (isSelected) Color.Red else Color.Gray
                )
                Text(
                    text = stringResource(R.string.ordenes),
                    color = if (isSelected) Color.Red else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}







@Composable
fun PantallaOrdenes() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de Órdenes")
    }
}

