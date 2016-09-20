package com.example.juangui.un_app;

class Service {
    String nombre;
    String poster;
    String hora_salida;
    String lugar_salida;
    String origen;
    String lugar_llegada;
    String destino;
    String vehiculo;
    String placa;
    String capacidad;
    String quota;

    public Service(String nombre, String poster, String quota,
                   String capacidad, String vehiculo,
                   String hora_salida,
                   String lugar_salida, String origen,
                   String lugar_llegada, String destino, String placa) {

        this.nombre = nombre;
        this.poster = poster;
        this.quota = quota;
        this.capacidad = capacidad;
        this.vehiculo = vehiculo;
        this.hora_salida = hora_salida;
        this.lugar_salida = lugar_salida;
        this.origen = origen;
        this.lugar_llegada = lugar_llegada;
        this.destino = destino;
        this.placa = placa;
    }
}