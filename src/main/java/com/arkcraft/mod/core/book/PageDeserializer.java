package com.arkcraft.mod.core.book;

import java.lang.reflect.Type;

import com.arkcraft.mod.core.lib.LogHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PageDeserializer implements JsonDeserializer<IPage> {

	@Override
	public IPage deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) {
		JsonObject jObject = json.getAsJsonObject();
		try {
			LogHelper.info("Deserializing Objects! PageDeserializer.deserialize() called!");
			Class<? extends IPage> pageClass = PageData.getPageClass(jObject.get("type").getAsString());
			LogHelper.info("Reached after pageClass.");
			LogHelper.info(pageClass == null ? "Page Class is null!" : "Page class is not null.");
			return context.deserialize(jObject, pageClass);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
