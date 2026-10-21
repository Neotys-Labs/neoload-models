package com.neotys.neoload.model.v3.binding.serializer;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

/**
 * The Real Browser steps ({@code browser_click}...) are {@link com.neotys.neoload.model.v3.project.userpath.CustomAction}s
 * whose type is given by the step name instead of a {@code type} property. Their name is optional and defaults to the
 * action name displayed in NeoLoad GUI.
 */
final class RealBrowserSteps {
	private static final ImmutableBiMap<String, String> TYPES_BY_STEP;
	private static final ImmutableMap<String, String> DEFAULT_NAMES_BY_TYPE;
	static {
		final ImmutableBiMap.Builder<String, String> builder = ImmutableBiMap.builder();
		final ImmutableMap.Builder<String, String> names = ImmutableMap.builder();
		register(builder, names, "browser_open", "BrowserOpen", "OpenBrowser");
		register(builder, names, "browser_close", "BrowserClose", "CloseBrowser");
		register(builder, names, "browser_navigate", "BrowserNavigate", "Navigate");
		register(builder, names, "browser_click", "BrowserClick", "Click");
		register(builder, names, "browser_type", "BrowserType", "Type");
		register(builder, names, "browser_press", "BrowserPress", "Press");
		register(builder, names, "browser_select_option", "BrowserSelectOption", "SelectOption");
		register(builder, names, "browser_mouse_over", "BrowserMouseOver", "MouseOver");
		register(builder, names, "browser_drag_and_drop", "BrowserDragAndDrop", "Drag & Drop");
		register(builder, names, "browser_scroll", "BrowserScroll", "Scroll");
		register(builder, names, "browser_read", "BrowserRead", "Read");
		register(builder, names, "browser_wait_selector", "BrowserWaitSelector", "WaitSelector");
		register(builder, names, "browser_evaluate_javascript", "BrowserEvaluateJavascript", "EvaluateJavascript");
		register(builder, names, "browser_upload_file", "BrowserUploadFile", "UploadFile");
		register(builder, names, "browser_handle_dialog", "BrowserHandleDialog", "HandleBrowserDialog");
		register(builder, names, "browser_open_tab", "BrowserOpenTab", "OpenTab");
		register(builder, names, "browser_switch_tab", "BrowserSwitchTab", "SwitchTab");
		register(builder, names, "browser_close_tab", "BrowserCloseTab", "CloseTab");
		register(builder, names, "browser_capture_requests", "BrowserCaptureRequests", "CaptureRequests");
		register(builder, names, "browser_capture_web_vitals", "BrowserCaptureWebVitals", "CaptureWebVitals");
		TYPES_BY_STEP = builder.build();
		DEFAULT_NAMES_BY_TYPE = names.build();
	}

	private RealBrowserSteps() {
		super();
	}

	private static void register(final ImmutableBiMap.Builder<String, String> types, final ImmutableMap.Builder<String, String> names,
			final String step, final String type, final String defaultName) {
		types.put(step, type);
		names.put(type, defaultName);
	}

	/** @return the custom action type of the step, or {@code null} when the step is not a Real Browser step */
	static String typeOf(final String stepName) {
		return TYPES_BY_STEP.get(stepName);
	}

	/** @return the step name of the custom action type, or {@code null} when the type is not a Real Browser action */
	static String stepOf(final String type) {
		return TYPES_BY_STEP.inverse().get(type);
	}

	/** @return the name given to a Real Browser action of that type when none is written */
	static String defaultNameOf(final String type) {
		return DEFAULT_NAMES_BY_TYPE.get(type);
	}
}
