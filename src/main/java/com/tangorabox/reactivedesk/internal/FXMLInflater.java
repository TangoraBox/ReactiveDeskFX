package com.tangorabox.reactivedesk.internal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class FXMLInflater {

	private final Provider<FXMLLoader> fxmlLoaderProvider;

	public static void inflate(Node component) {
		FXMLInflater inflater = new FXMLInflater(FXMLLoader::new);
		inflater.loadNestedFXMLOfComponent(component);
	}

	@Inject
	FXMLInflater(Provider<FXMLLoader> fxmlLoaderProvider) {
		super();
		this.fxmlLoaderProvider = fxmlLoaderProvider;
	}
	

	public URL getNestedFXMLOfComponent(Node component) {
		Class<? extends Node> componentClass = component.getClass();
		String className = componentClass.getName().replace('.', '/');
		String baseName = className + ".fxml";
		return componentClass.getClassLoader().getResource(baseName);
	}

	public void loadNestedFXMLOfComponent(Node component) {
		FXMLLoader loader = fxmlLoaderProvider.get();
		URL xmlLocation = getNestedFXMLOfComponent(component);
		if (xmlLocation == null) {
			return;
		}
		loadFXMLForComponent(xmlLocation, component, loader);
	}

	private Optional<ResourceBundle> getNestedResourceBundleOfComponent(Node component) {
		try {
			return Optional.of(ResourceBundle.getBundle(component.getClass().getName()));
		} catch (MissingResourceException ex) {
			return Optional.empty();
		}
	}

	private void loadFXMLForComponent(URL xmlLocation, Node component, FXMLLoader loader) {
		loader.setLocation(xmlLocation);
		loader.setRoot(component);
		loader.setController(component);
		getNestedResourceBundleOfComponent(component)
				.ifPresent(loader::setResources);
		try {
			InputStream preloadedXML = preloadAndFilterController(xmlLocation);
			loader.load(preloadedXML);
		} catch (IOException ex) {
			throw new UncheckedIOException("fxml load error:", ex);
		}
	}

	private InputStream preloadAndFilterController(URL xmlLocation) throws IOException {
		try (InputStream inputStream = xmlLocation.openStream()) {
			String xmlString = convertToString(inputStream);
			String xmlFilteredString = xmlString.replaceAll("fx:controller.*=.*\".*?\"", "");
			return new ByteArrayInputStream(xmlFilteredString.getBytes(StandardCharsets.UTF_8));
		}
	}

	private String convertToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString(StandardCharsets.UTF_8.name());
	}
}
