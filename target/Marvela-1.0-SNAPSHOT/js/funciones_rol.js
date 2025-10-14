$(function () {
    cargarTabla(); //Es para llamar la funcion donde se esta mandando a llamar los id y parametros

    $(document).on("submit", "#formulario_registro", function (e) {
        e.preventDefault();
        var datos = $("#rol").val();
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "RolServlet",
            data: {"rol": datos, "consultar_datos": "si_registro"}
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#rol").val("");
                $("#md_registrar").modal("hide");
                cargarTabla();
                Swal.fire("Excelente", "El dato fue agregado", "success");
            } else if (json[0].resultado === "error_duplicado") {
                Swal.fire('Error', json[0].mensaje, 'error');
            } else {
                Swal.fire('Error', "No se puede registrar un nuevo rol", "error");
            }
        }).fail(function () {
            Swal.fire('Error', "Error de conexión", "error");
        }).always(function () {
        });
    });

    $(document).on("submit", "#formulario_editar", function (e) {
        e.preventDefault();
        var codigo_rol = $("#codigo_rol").val();
        var rolE = $("#rolE").val();

        $.ajax({
            dataType: "json",
            method: "POST",
            url: "RolServlet",
            data: {"codigo_rol": codigo_rol, "rolE": rolE, "consultar_datos": "editar"}
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#codigo_rol").val("");
                $("#rolE").val("");
                $("#myModalE").modal("hide");
                cargarTabla();
                Swal.fire("Excelente", "El dato fue modificado", "success");
            } else if (json[0].resultado === "error_duplicado") {
                Swal.fire('Error', json[0].mensaje, 'error');
            } else {
                Swal.fire('Error', "No se puede modificar el rol", "error");
            }
        }).fail(function () {
            Swal.fire('Error', "Error de conexión", "error");
        }).always(function () {
        });
    });

    $(document).on("click", ".btn_editar", function (e) {
        e.preventDefault();
        cargarDatosModal($(this).attr('data-id'));
    });

    function cargarTabla(estado = 1) {
        var datos = {"consultar_datos": "si_consulta", "estado": estado};

        $.ajax({
            dataType: "json",
            method: "POST",
            url: "RolServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado == "exito") {
                // Destruir el DataTable si ya existe
                if ($.fn.DataTable.isDataTable("#tabla_roles")) {
                    $("#tabla_roles").DataTable().destroy();
                }

                $("#rolesA").empty().html(json[0].tabla);

                // Inicializar DataTable con las opciones
                var table = $("#tabla_roles").DataTable({
                    "language": {
                        "url": "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"
                    },
                    lengthMenu: [[5, 10, 15, -1], [5, 10, 15, "Todos"]],
                    pageLength: 5,
                    columnDefs: [
                        {responsivePriority: 1, targets: 0},
                        {responsivePriority: 2, targets: -1}
                    ],
                    "destroy": true, // Asegura que se destruya antes de inicializar
                    "retrieve": true // Permite recuperar la instancia existente
                });
            } else {
                Swal.fire('Error', 'No pudo obtener los datos', 'error');
            }
        }).fail(function () {
            Swal.fire('Error', 'Error de conexión al cargar la tabla', 'error');
        }).always(function () {
        });
    }

    function cargarDatosModal(codigo_rol) {
        var datos = {"consultar_datos": "cargarDatos", "codigo_rol": codigo_rol};
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "RolServlet",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $('#codigo_rol').val(json[0].codigo_rol);
                $('#rolE').val(json[0].rolE);
                $('#myModalE').modal('show');
            } else {
                Swal.fire(
                        'Error',
                        'No existe el Identificador en la Base Datos de la Tabla',
                        'error'
                        );
            }
        }).fail(function () {
            Swal.fire('Error', 'Error de conexión al cargar datos', 'error');
        }).always(function () {
        });
    }

    $(document).on("click", "#btn_cerrar", function (e) {
        e.preventDefault();
        $("#md_registrar").modal("hide");
    });

    function mostrar_cargando(titulo, mensaje = "") {
        Swal.fire({
            title: titulo,
            html: mensaje,
            timer: 2000,
            timerProgessBar: true,
            didOpen: () => {
                Swal.showLoading();
            },
            willClose: () => {
            }
        }).then((result) => {
            if (result.dismiss === Swal.DismissReason.timer) {
                console.log('I was closed by timer');
            }
        });
    }

    // Función para validar nombre en tiempo real
    function validarNombre(input) {
        const errorElement = document.getElementById(`error-${input.id}`);
        const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/; // Solo letras y espacios

        if (!regex.test(input.value)) {
            input.classList.add('is-invalid');
            input.classList.remove('is-valid');
            if (errorElement)
                errorElement.style.display = 'block';
            return false;
        } else {
            input.classList.remove('is-invalid');
            input.classList.add('is-valid');
            if (errorElement)
                errorElement.style.display = 'none';
            return true;
        }
    }

    // Validación al enviar el formulario de registro
    document.getElementById('formulario_registro').addEventListener('submit', function (e) {
        const rolInput = document.getElementById('rol');
        if (!validarNombre(rolInput)) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Error de validación',
                text: 'El nombre del rol no puede contener números ni caracteres especiales'
            });
        }
    });

    // Validación al enviar el formulario de edición
    document.getElementById('formulario_editar').addEventListener('submit', function (e) {
        const rolInput = document.getElementById('rolE');
        if (!validarNombre(rolInput)) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Error de validación',
                text: 'El nombre del rol no puede contener números ni caracteres especiales'
            });
        }
    });

    // Limpiar validación al cerrar modales
    document.querySelectorAll('.modal').forEach(modal => {
        modal.addEventListener('hidden.bs.modal', function () {
            const inputs = this.querySelectorAll('input');
            inputs.forEach(input => {
                input.classList.remove('is-invalid', 'is-valid');
                const errorElement = document.getElementById(`error-${input.id}`);
                if (errorElement)
                    errorElement.style.display = 'none';
            });
        });
    });

    // Función de validación con setCustomValidity
    function validarNombreConCustomValidity(input) {
        const valor = input.value.trim();
        const patron = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;

        if (valor === "") {
            input.setCustomValidity("Este campo no puede estar vacío.");
        } else if (!patron.test(valor)) {
            input.setCustomValidity("Solo se permiten letras y espacios.");
        } else {
            input.setCustomValidity(""); // válido
        }
    }

    // Aplicar validación en tiempo real a los campos
    $('#rol').on('input', function () {
        validarNombreConCustomValidity(this);
        validarNombre(this);
    });

    $('#rolE').on('input', function () {
        validarNombreConCustomValidity(this);
        validarNombre(this);
    });

    // Modo oscuro toggle
    const toggle = document.getElementById('modoOscuroToggle');
    if (toggle) {
        toggle.addEventListener('click', () => {
            document.body.classList.toggle('oscuro');
        });
    }
});


