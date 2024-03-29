package com.msvi.banco.Repositories;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msvi.banco.Clases.Cliente;
import com.msvi.banco.Interfaces.IAcceso;
import com.msvi.banco.Interfaces.IActualizarCliente;
import com.msvi.banco.Interfaces.IAgregarCliente;
import com.msvi.banco.Interfaces.IConsultarCliente;
import com.msvi.banco.Interfaces.IEliminarCliente;
import com.msvi.banco.Interfaces.IListarClientes;
import com.msvi.banco.Interfaces.IReturnCreateAcount;
import com.msvi.banco.Interfaces.IReturnTransferencia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApiBanco
{

    private String url = "http://apibanco.tk/api.php";
    private String correo;
    private String clave;
    private String receive;

    public void acceso(String email, String password, Context context, final IAcceso callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        this.correo = email;
        this.clave = password;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccessResponse(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onSuccessResponse(error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "acceso");
                params.put("email", correo);
                params.put("password", clave);
                return params;
            }
        };

        queue.add(strRequest);
    }

    public void listarClientes(Context context, final IListarClientes callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                ArrayList<Cliente> customers = new ArrayList<>();
                JSONObject cliente;
                JSONObject resp;
                JSONArray clientes;
                Cliente clienteObtenido;

                try
                {
                    resp = new JSONObject(response);
                    clientes = resp.getJSONArray("users");

                    for(int i = 0; i < clientes.length(); i++)
                    {
                        clienteObtenido = new Cliente();
                        cliente = clientes.getJSONObject(i);
                        clienteObtenido.setIdent(cliente.getString("ident"));
                        clienteObtenido.setNombres(cliente.getString("nombres"));
                        clienteObtenido.setEmail(cliente.getString("email"));
                        clienteObtenido.setClave(cliente.getString("clave"));
                        customers.add(clienteObtenido);
                    }
                    callback.onReturnCustomers(customers);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "listarClientes");
                return params;
            }
        };

        queue.add(strRequest);
    }

    public void agregarCliente(Context context, final Cliente cliente, final IAgregarCliente callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onResponseAddCustomer(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("tipo", "insertar");
                params.put("ident", cliente.getIdent());
                params.put("nombres", cliente.getNombres());
                params.put("email", cliente.getEmail());
                params.put("clave", cliente.getClave());

                return params;
            }
        };

        queue.add(strRequest);
    }

    public void consultarCliente(Context context, final String ident, final IConsultarCliente callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onResponseCustomer(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "consultarCuenta");
                params.put("ident", ident);
                return params;
            }
        };
        queue.add(strRequest);
    }

    public void actualizarCliente(Context context, final Cliente cliente, final IActualizarCliente callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onResponseStatus(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("tipo", "actualizar");
                params.put("ident", cliente.getIdent());
                params.put("nombres", cliente.getNombres());
                params.put("email", cliente.getEmail());
                params.put("clave", cliente.getClave());

                return params;
            }
        };

        queue.add(strRequest);
    }
    public void eliminarCliente(Context context, final String ident, final IEliminarCliente callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.w("msvi", response);
                callback.onResponseStatus(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "eliminar");
                params.put("ident", ident);
                return params;
            }
        };

        queue.add(strRequest);
    }

    public void crearCuenta(Context context, final String ident, final String saldo, final IReturnCreateAcount callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onReturnCreateAcount(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "crearCuenta");
                params.put("ident", ident);
                params.put("saldo", saldo);
                return params;
            }
        };

        queue.add(strRequest);
    }

    public void realizarTransaccion(Context context, final String cuentaOrigen, final String cuentaDestino, final String valor, final IReturnTransferencia callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onResponseTransfer(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "realizarTransaccion");
                params.put("cuentaOrigen", cuentaOrigen);
                params.put("cuentaDestino", cuentaDestino);
                params.put("valor", valor);
                return params;
            }
        };

        queue.add(strRequest);
    }
    public void consultarTransacciones(Context context, final String ident, final IReturnTransferencia callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onResponseTransfer(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "consultarTransacciones");
                params.put("ident", ident);
                return params;
            }
        };

        queue.add(strRequest);
    }

}
