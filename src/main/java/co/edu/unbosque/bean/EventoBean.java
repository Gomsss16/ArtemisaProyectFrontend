package co.edu.unbosque.bean;

import co.edu.unbosque.dto.EventoDTO;
import co.edu.unbosque.service.EventoService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.schedule.ScheduleEntryMoveEvent;
import org.primefaces.event.schedule.ScheduleEntryResizeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ViewScoped
@Named("eventoBean")
public class EventoBean implements Serializable {

	private static final long serialVersionUID = 1L;


	private List<EventoDTO> eventos = new ArrayList<>();
	private String titulo = "";
	private String descripcion = "";
	private String tipo = "";
	private Date fecha;
	private String enlace = "";
	private String ubicacion = "";

	private ScheduleModel eventModel;
	private ScheduleEvent<?> event = new DefaultScheduleEvent<>();

	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
			.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				@Override
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
					try {
						return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getAsString());
					} catch (ParseException e) {
						try {
							return new SimpleDateFormat("yyyy-MM-dd").parse(json.getAsString());
						} catch (ParseException e2) {
							return null;
						}
					}
				}
			}).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
				@Override
				public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
					return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(src));
				}
			}).create();

	public EventoBean() {
		// Constructor vacío
	}

	@PostConstruct
	public void init() {
		cargarEventos();
		inicializarCalendario();
	}

	public void cargarEventos() {
		try {
			System.out.println("=== CARGANDO EVENTOS ===");
			String respuesta = EventoService.doGet("http://localhost:8081/evento/getall");

			if (respuesta != null && !respuesta.contains("Error")) {
				String[] partes = respuesta.split("\n", 2);

				if (partes.length > 1 && !partes[1].equals("[]")) {
					try {
						Type listType = new TypeToken<List<EventoDTO>>() {
						}.getType();
						List<EventoDTO> lista = gson.fromJson(partes[1], listType);

						if (lista != null) {
							eventos = lista.stream()
									.filter(e -> e.getTitulo() != null && !e.getTitulo().trim().isEmpty())
									.collect(Collectors.toList());
						} else {
							eventos = new ArrayList<>();
						}

						System.out.println("Eventos válidos cargados: " + eventos.size());

					} catch (Exception e) {
						System.err.println("Error parseando JSON: " + e.getMessage());
						eventos = new ArrayList<>();
					}
				} else {
					eventos = new ArrayList<>();
				}
			} else {
				eventos = new ArrayList<>();
			}

		} catch (Exception e) {
			System.err.println("Error cargando eventos: " + e.getMessage());
			eventos = new ArrayList<>();
		}
	}

	private void inicializarCalendario() {
		eventModel = new DefaultScheduleModel();

		// Convertir eventos existentes al calendario
		for (EventoDTO evento : eventos) {
			if (evento.getFecha() != null) {
				LocalDateTime fechaInicio = evento.getFecha().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDateTime();

				eventModel.addEvent(DefaultScheduleEvent.builder().title(evento.getTitulo()).startDate(fechaInicio)
						.endDate(fechaInicio.plusHours(2)).description(evento.getDescripcion()).data(evento.getId())
						.styleClass("event-" + (evento.getTipo() != null ? evento.getTipo() : "general")).build());
			}
		}

		if (eventos.isEmpty()) {
			LocalDateTime now = LocalDateTime.now();

			eventModel.addEvent(DefaultScheduleEvent.builder().title("Competencia ACM Regional 2025")
					.startDate(now.plusDays(7).withHour(9).withMinute(0))
					.endDate(now.plusDays(7).withHour(12).withMinute(0))
					.description("Competencia de programación estilo ACM-ICPC").styleClass("event-acm").build());

			eventModel.addEvent(DefaultScheduleEvent.builder().title("Taller de Algoritmos")
					.startDate(now.plusDays(14).withHour(14).withMinute(0))
					.endDate(now.plusDays(14).withHour(16).withMinute(0))
					.description("Taller sobre estructuras de datos avanzadas").styleClass("event-taller").build());

			eventModel.addEvent(DefaultScheduleEvent.builder().title("Hackathon Universitario")
					.startDate(now.plusDays(21).withHour(8).withMinute(0))
					.endDate(now.plusDays(21).withHour(18).withMinute(0))
					.description("Competencia de desarrollo de software").styleClass("event-hackathon").build());
		}
	}

	public void addEvento() {
		try {
			System.out.println("=== CREANDO EVENTO ===");

			if (titulo == null || titulo.trim().isEmpty()) {
				showMessage("Error", "El título es obligatorio");
				return;
			}

			if (fecha == null) {
				showMessage("Error", "La fecha es obligatoria");
				return;
			}

			Map<String, Object> eventoParaJson = new HashMap<>();
			eventoParaJson.put("titulo", titulo.trim());
			eventoParaJson.put("descripcion", descripcion != null ? descripcion.trim() : "");
			eventoParaJson.put("tipo", tipo != null ? tipo : "evento");
			eventoParaJson.put("fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha));
			eventoParaJson.put("enlace", enlace != null ? enlace.trim() : "");
			eventoParaJson.put("ubicacion", ubicacion != null ? ubicacion.trim() : "");

			String json = gson.toJson(eventoParaJson);
			String respuesta = EventoService.doPost("http://localhost:8081/evento/createeventojson", json);

			if (respuesta != null && respuesta.startsWith("201")) {
				showMessage("201", "Evento '" + titulo + "' creado exitosamente");
				limpiarCampos();
				cargarEventos();
				inicializarCalendario();

			} else if (respuesta != null && respuesta.contains("ya existe")) {
				showMessage("409", "El evento '" + titulo + "' ya existe");

			} else {
				showMessage("Error", "Error del servidor: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error interno: " + e.getMessage());
		}
	}

	public void eliminarPorId(Long id) {
		try {
			System.out.println("=== ELIMINANDO EVENTO POR ID: " + id + " ===");

			if (id == null) {
				showMessage("Error", "Error: ID es null");
				return;
			}

			EventoDTO eventoAEliminar = null;
			for (EventoDTO e : eventos) {
				if (e.getId() != null && e.getId().equals(id)) {
					eventoAEliminar = e;
					break;
				}
			}

			if (eventoAEliminar == null) {
				showMessage("Error", "No se encontró el evento con ID: " + id);
				return;
			}

			String titleEncoded = java.net.URLEncoder.encode(eventoAEliminar.getTitulo(), "UTF-8");
			String url = "http://localhost:8081/evento/deletebyTitle?title=" + titleEncoded;

			String respuesta = EventoService.doDelete(url);

			if (respuesta != null && (respuesta.startsWith("200") || respuesta.startsWith("202"))) {
				showMessage("200", "Evento '" + eventoAEliminar.getTitulo() + "' eliminado");
				cargarEventos();
				inicializarCalendario();
			} else {
				showMessage("Error", "Error eliminando: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error: " + e.getMessage());
		}
	}

	public boolean esEstudiante() {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);

			if (session != null) {
				String role = (String) session.getAttribute("role");
				return "Estudiante".equals(role);
			}

			return true;
		} catch (Exception e) {
			return true;
		}
	}

	public String formatearFecha(Date fecha) {
		if (fecha == null)
			return "";
		return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
	}

	public String getTipoEvento(String tipo) {
		if (tipo == null)
			return "Evento";
		switch (tipo.toLowerCase()) {
		case "acm":
			return "Competencia ACM";
		case "taller":
			return "Taller";
		case "hackathon":
			return "Hackathon";
		case "conferencia":
			return "Conferencia";
		default:
			return "Evento";
		}
	}

	private void limpiarCampos() {
		titulo = "";
		descripcion = "";
		tipo = "";
		fecha = null;
		enlace = "";
		ubicacion = "";
	}

	public void showMessage(String code, String content) {
		FacesContext context = FacesContext.getCurrentInstance();

		if ("201".equals(code) || "200".equals(code)) {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Sistema Artemisa", content));
		} else if ("409".equals(code)) {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", content));
		} else if ("404".equals(code)) {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, "No Encontrado", content));
		} else {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", content));
		}
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleEvent<?> getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent<?> event) {
		this.event = event;
	}
	public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
	    ScheduleEvent<?> selectedEvent = selectEvent.getObject();
	    
	    System.out.println("=== EVENTO SELECCIONADO ===");
	    System.out.println("Título: " + selectedEvent.getTitle());
	    System.out.println("Inicio: " + selectedEvent.getStartDate());
	    System.out.println("Fin: " + selectedEvent.getEndDate());
	    
	    event = DefaultScheduleEvent.builder()
	            .title(selectedEvent.getTitle() != null ? selectedEvent.getTitle() : "Sin título")
	            .startDate(selectedEvent.getStartDate())
	            .endDate(selectedEvent.getEndDate())
	            .description(selectedEvent.getDescription() != null ? selectedEvent.getDescription() : "Sin descripción disponible")
	            .build();
	            
	    System.out.println("Evento creado para mostrar: " + event.getTitle());
	}


	public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
		event = DefaultScheduleEvent.builder().startDate(selectEvent.getObject())
				.endDate(selectEvent.getObject().plusHours(1)).build();
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		System.out.println("Evento movido: " + event.getScheduleEvent().getTitle());
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		System.out.println("Evento redimensionado: " + event.getScheduleEvent().getTitle());
	}

	public List<EventoDTO> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoDTO> eventos) {
		this.eventos = eventos;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
}
