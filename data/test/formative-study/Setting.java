// BLOCKTEST UNSUPPORTED EXAMPLE: https://github.com/jeroenvanmaanen/common/blob/d646f41f42bab86c88a90e232421c4d2e88fa9ef/src/main/java/org/leialearns/common/Setting.java#L80-L87
package org.leialearns.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.leialearns.common.Display.display;
import static org.leialearns.common.Display.displayParts;
import static org.leialearns.common.Static.getLoggingClass;

import static org.blocktest.BTest.blocktest;
import org.blocktest.BTest;
import static org.blocktest.types.EndAt.*;

/**
* Encapsulates a reference to a value that can be set only once. Declare a <code>final Setting&lt;T&gt;</code> instead
* of a <code>final T</code> property, if the value is not known when the object is constructed, but otherwise behaves
* like a <code>final</code> property. A setting reference is thread-safe.
* @param <T> The type of the value
*/
public class Setting<T> {
	private final Logger logger = getLogger();
	private final String name;
	private final Supplier<T> defaultExpression;
	private final Object lock = new Object();
	private volatile T value = null;
	
	/**
	* Creates a new <code>Setting</code> instance. The default value is used in case the setting is not set before the
	* getter is called.
	* @param name A label for the setting
	* @param defaultExpression An expression that lazily returns a default value
	*/
	public Setting(String name, Supplier<T> defaultExpression) {
		this.name = name;
		this.defaultExpression = defaultExpression;
	}
	
	/**
	* Creates a new <code>Setting</code> instance. The default value is used in case the setting is not set before the
	* getter is called.
	* @param name A label for the setting
	* @param defaultValue The default value
	*/
	public Setting(String name, final T defaultValue) {
		this(name, () -> defaultValue);
	}
	
	/**
	* Creates a new <code>Setting</code> instance.
	* @param name A label for the setting
	*/
	public Setting(String name) {
		this(name, (T) null);
	}
	
	/**
	* Sets the value, if possible.
	* @param value The value to set
	* @throws java.lang.IllegalStateException If the reference was already set to another value
	*/
	public void set(T value) {
		if (logger.isTraceEnabled()) {
			logger.trace("Name: " + display(name) + ": Value: " + display(value), new Throwable("Stack trace"));
		}
		T thisValue = offerInternal(value);
		if (thisValue != null) {
			throw new IllegalStateException("Value was already set to another value: " + name + " == [" + display(thisValue) + "], not: [" + display(value) + "]");
		}
	}
	
	/**
	* Returns the value of this setting. If the values was not set, it is set to the (lazily evaluated) default value
	* first. If the expression that calculates the default value returns null, an <code>IllegalStateException</code>
	* is thrown.
	* @return The value of this setting
	* @throws java.lang.IllegalStateException When the value is not set
	*/
	public T get() {
		return getInternal(
				x -> x,
				() -> {
					blocktest().given(defaultExpression, new Supplier<String>()).expect(IllegalStateException.class);
					T defaultValue = defaultExpression.get();
					if (defaultValue == null) {
						throw new IllegalStateException("Value is not set: " + name);
					}
					value = defaultValue;
					return defaultValue;
				}
		);
	}
	
	/**
	* Returns a flag that indicates whether the value of this setting is already fixated.
	* @return <code>true</code> if the value is fixated; <code>false</code> otherwise
	*/
	public boolean isFixated() {
		return getInternal(
				x -> true,
				() -> false
		);
	}
	
	protected <Q> Q getInternal(Function<T,Q> successCase, Supplier<Q> failureCase) {
		Q result = null;
		T thisValue = this.value;
		if (thisValue == null) {
			synchronized (lock) {
				// Double checked locking: needs J2SE5
				thisValue = this.value;
				if (thisValue == null) {
					result = failureCase.get();
					if (logger.isDebugEnabled()) {
						logger.debug("Use default value for setting: " + name + ": " + display(result));
					}
					if (logger.isTraceEnabled()) {
						logger.trace("Stack trace" ,new Throwable());
					}
				}
			}
		}
		if (thisValue != null) {
			result = successCase.apply(thisValue);
		}
		return result;
	}
	
	/**
	* Offers the value as a candidate for the reference.
	* @param value The candidate value for this setting
	* @return <code>true</code> if the given value is equal to the value of the setting; <code>false</code> otherwise
	*/
	public boolean offer(T value) {
		return offerInternal(value) == null;
	}
	
	protected T offerInternal(T value) {
		if (value == null) {
			throw new IllegalArgumentException("Value should not be null: " + name);
		}
		T thisValue = this.value;
		if (thisValue == null) {
			synchronized (lock) {
				// Double checked locking: needs J2SE5
				thisValue = this.value;
				if (thisValue == null) {
					this.value = value;
					logger.trace("Set [" + name + "] to: [" + value + "]");
				}
			}
		}
		logger.trace("Compare [" + thisValue + "] to: [" + value + "]");
		if (thisValue == null) {
			logger.trace("Null"); // Okay
		} else if (thisValue == value) {
			thisValue = null;
		} else if (value.equals(thisValue)) {
			logger.warn("Weak identity: [" + thisValue + "] equals [" + value + "]");
			thisValue = null;
		} else {
			logger.debug("Mismatch: [" + thisValue + "] differs from [" + value + "]");
		}
		return thisValue;
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getLoggingClass(this));
	}
	
	public String toString() {
		return displayParts("Setting", value);
	}
	
}
