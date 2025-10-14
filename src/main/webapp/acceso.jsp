

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap" rel="stylesheet"/>
        <!-- MDB -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet"/>
        <style>
            .gradient-custom-2 {
                background: rgb(32,32,32);
                background: linear-gradient(90deg, rgba(32,32,32,1) 0%, rgba(247,7,7,1) 50%, rgba(247,130,71,1) 100%);
            }

            @media (min-width: 768px) {
                .gradient-form {
                    height: 100vh !important;
                }
            }
            @media (min-width: 769px) {
                .gradient-custom-2 {
                    border-top-right-radius: .3rem;
                    border-bottom-right-radius: .3rem;
                }
            }
            
                .bg-green-gradient {
  background: linear-gradient(135deg, #56ab2f, #a8e063);
}

        </style>
    </head>
    <body>
   <section class="h-100 gradient-form" style="background-color: #9a9a9a;">
  <div class="container py-2 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-xl-8">
        <div class="card rounded-3 text-black">
          <div class="row g-0">
            <!-- Columna izquierda: formulario -->
            <div class="col-lg-6">
              <div class="card-body p-md-5 mx-md-6">
                <div class="text-center">
                  <img src="assets/img/logo.jpg" style="width: 100px; height: 100px;" alt="logo">
                  <h4 class="mt-1 mb-3 pb-1">Iniciar Sesión</h4>
                </div>

                <form name="login" id="ingresar">
                  <div class="form-outline mb-3">
                    <input type="text" name="usuario" id="usuario" class="form-control" required />
                    <label class="form-label" for="form2Example11">Correo</label>
                  </div>

                  <div class="form-outline mb-3">
                    <input type="password" name="clave" id="clave" class="form-control" required />
                    <label class="form-label" for="form2Example22">Contraseña</label>
                  </div>

                  <div class="text-center pt-3 mb-0 pb-1">
                    <button class="btn btn-primary btn-block fa-lg gradient-custom-2 mb-3" type="submit" name="accion" value="Ingresar">Ingresar</button>
                  </div>
                </form>
              </div>
            </div>

            <!-- Columna derecha: estilo verde aplicado -->
            <div class="col-lg-6 d-flex align-items-center bg-green-gradient">
              <div class="text-center text-white px-3 py-4 p-md-5 mx-md-4">
                <h4 class="mb-4">GESTIÓN DE TAREAS</h4>
                <p class="small mb-3">ASIGNACIÓN INTELIGENTE, RESULTADOS BRILLANTES</p>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</section>
    
        <!-- MDB -->
        <script src="assets/js/jquery-3.6.0.min.js"></script>
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
        <script src="assets/plugins/sweetalert/sweetalert2.all.min.js"></script>
        <!-- SweetAlerts - Otro script relacionado con SweetAlert -->
        <script src="assets/plugins/sweetalert/sweetalerts.min.js"></script>
        <script type="text/javascript" src="js/acceso.js"></script>
    </body>
</html>
