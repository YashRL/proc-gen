package fr.theorozier.procgen.util;

public class ErrorUtils {

	public static IllegalArgumentException invalidUidArgument(String obj) {
		return new IllegalArgumentException(obj + "'s UID can't be less or equals than zero.");
	}

}
