package com.tangorabox.reactivedesk;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValueBase;

import java.util.*;

public class ReactiveModel<T> extends ObservableValueBase<T> implements InvalidationListener {

	public static final String ERROR_MESSAGE = "Model hasn't observable properties, set it first!!";
	private Observable[] observableProperties = null;
	private final List<InvalidationListener> invalidationListeners = new LinkedList<>();

	public ReactiveModel() {
		super();
	}

	protected void setObservableProperties(Observable... properties) {
		if (this.observableProperties != null) {
			throw new IllegalStateException("Model yet initialized, can't set properties again");
		}
		this.observableProperties = Objects.requireNonNull(properties, "Properties cannot be null!");
		for (Observable property : properties) {
			property.addListener(this);
		}
	}

	@Override
	public void invalidated(Observable observable) {
		fireValueChangedEvent();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		super.addListener(listener);
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		super.removeListener(listener);
		invalidationListeners.remove(listener);
	}

	@Override
	public void addListener(ChangeListener<? super T> listener) {
		throw new UnsupportedOperationException("Only invalidation listener supported");
	}

	@Override
	public T getValue() {
		return (T) this;
	}

	public Observable[] getObservableProperties() {
		return Objects.requireNonNull(observableProperties, ERROR_MESSAGE);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ReactiveModel<?> that = (ReactiveModel<?>) o;
		return Arrays.equals(observableProperties, that.observableProperties);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(observableProperties);
	}

	public void dispose() {
		for (Observable observableProperty : getObservableProperties()) {
			observableProperty.removeListener(this);
		}
		observableProperties = null;

		List<InvalidationListener> listCopy = new ArrayList<>(invalidationListeners);
		for (InvalidationListener invalidationListener : listCopy) {
			removeListener(invalidationListener);
		}
		listCopy.clear();
		invalidationListeners.clear();
	}
}
