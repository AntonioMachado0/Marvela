<%-- 
    Document   : registrar_empleado
    Created on : 29 oct. 2025, 08:42:15
    Author     : Maris
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Empleado - Ferretería Marvela</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet"/>

        <style>
            body {
                background: linear-gradient(135deg, #c8e6c9, #a5d6a7, #81c784);
                background-size: 600% 600%;
                animation: fondoMovimiento 20s ease infinite;
                font-family: 'Roboto', sans-serif;
            }

            @keyframes fondoMovimiento {
                0% {
                    background-position: 0% 50%;
                }
                100% {
                    background-position: 100% 50%;
                }
            }

            .gradient-form {
                height: 100vh;
            }

            .logo-box {
                background-color: #ffffff;
                border-radius: 50%;
                padding: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                margin-bottom: 10px;
            }

            .btn-registrar {
                font-size: 14px;
                padding: 8px 16px;
            }

            .input-group-text {
                background-color: #388e3c;
                color: #fff;
                border: none;
            }

            .text-volver {
                font-size: 13px;
                color: #388e3c;
            }

            .text-volver:hover {
                text-decoration: underline;
                color: #2e7d32;
            }
        </style>
    </head>
    <body>

        <!-- Íconos flotantes -->
        <i class="fas fa-user-tie position-absolute" style="top: 10%; left: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-id-card position-absolute" style="bottom: 10%; right: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-calendar-alt position-absolute" style="top: 50%; left: 10%; font-size: 70px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-phone-alt position-absolute" style="bottom: 20%; right: 15%; font-size: 60px; color: rgba(0,0,0,0.05);"></i>

        <section class="gradient-form">
            <div class="container py-5 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-xl-6">
                        <div class="card rounded-3 text-black shadow">
                            <div class="card-body p-md-5 mx-md-4">
                                <div class="text-center">
                                    <div class="logo-box">
                                        <img src="img/Login2.png" style="width: 80px;" alt="Logo Ferretería">
                                    </div>
                                    <h4 class="mt-1 mb-4">Registro de Empleado</h4>
                                    <p class="mb-4">Completa los datos del nuevo empleado. El rol se asignará automáticamente.</p>
                                </div>

                                <form method="POST" action="UsuarioController">
                                    <input type="hidden" name="consultar_datos" value="registrarEmpleado">

                                    <div class="input-group mb-4">
                                        <span class="input-group-text">
                                            <i class="fas fa-user"></i>
                                        </span>
                                        <input type="text" name="nombre" class="form-control" placeholder="Nombre completo" required>
                                    </div>

                                    <div class="input-group mb-4">
                                        <span class="input-group-text">
                                            <i class="fas fa-calendar-day"></i>
                                        </span>
                                        <input type="date" name="fecha_nacimiento" class="form-control" required>
                                    </div>




                                    <div class="input-group mb-4">
                                        <span class="input-group-text">
                                            <i class="fas fa-phone"></i>
                                        </span>
                                        <input type="text" name="telefono" id="telefono" class="form-control" maxlength="9" placeholder="Teléfono (####-####)" required>
                                    </div>

                                    <script>
                                        const telefonoInput = document.getElementById("telefono");

                                        telefonoInput.addEventListener("input", function () {
                                            // Eliminar todo lo que no sea dígito
                                            let valor = this.value.replace(/\D/g, "");

                                            // Limitar a 8 dígitos
                                            if (valor.length > 8) {
                                                valor = valor.slice(0, 8);
                                            }

                                            // Insertar guion después del cuarto dígito
                                            if (valor.length > 4) {
                                                valor = valor.slice(0, 4) + "-" + valor.slice(4);
                                            }

                                            this.value = valor;
                                        });
                                    </script>
                                    <div class="text-center">
                                        <button class="btn btn-success btn-registrar btn-block mb-2" type="submit">
                                            <i class="fas fa-user-plus me-2"></i>Registrar empleado
                                        </button>
                                    </div>

                                    <div class="text-center">
                                        <a href="acceso.jsp" class="text-volver">← Volver al login</a>
                                    </div>
                                </form>

                                <c:if test="${not empty error}">
                                    <div class="text-center mt-3">
                                        <p style="color:red">${error}</p>
                                    </div>
                                </c:if>
                                <c:if test="${not empty mensaje}">
                                    <div class="text-center mt-3">
                                        <p style="color:green">${mensaje}</p>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
    </body>
</html>