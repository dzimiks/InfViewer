package metaschema;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.Attribute;
import models.Entity;
import models.InformationResource;
import models.Package;
import models.Relation;
import models.tree.Node;

public class Serializer {

	public Serializer() {
		
	}
	
	// Attribute
	public static JsonObject serializeAttribute(Attribute attr) {
		String name = attr.getName();
		Class<?> type = attr.getValueClass();
		int length = attr.getLength();
		boolean primaryKey = attr.isPrimaryKey();
		JsonObject obj = new JsonObject();

		obj.addProperty("name", name);
		obj.addProperty("type", type.toString());
		obj.addProperty("length", length);
		obj.addProperty("primaryKey", primaryKey);

		return obj;
	}

	public static Attribute deserializeAttribute(JsonObject obj) {
		String name = obj.get("name").getAsString();
		int length = obj.get("length").getAsInt();
		boolean primaryKey = obj.get("primaryKey").getAsBoolean();
		Attribute attr = new Attribute(name, primaryKey, length);
		return attr;
	}

	// Relation
	public static JsonObject serializeRelation(Relation rel) {
		ArrayList<Attribute> referencedAttributes = rel.getReferencedAttributes();
		ArrayList<Attribute> referringAttributes = rel.getReferringAttributes();
		Entity referencedEntity = rel.getReferencedEntity();
		JsonObject obj = new JsonObject();
		JsonArray referenced = new JsonArray();
		JsonArray referring = new JsonArray();
		
		for (Attribute attr : referencedAttributes) 
			referenced.add(referencedEntity.getName() + "/" + attr.getName());
		
		for (Attribute attr : referringAttributes)
			referring.add(attr.getName());
		
		obj.add("referencedAttributes", referenced);
		obj.add("referringAttributes", referring);
		
		return obj;
	}
	
	public static Relation deserializeRelation(JsonObject obj) {
		JsonArray referencedAttributes = obj.get("referencedAttributes").getAsJsonArray();
		JsonArray referringAttributes = obj.get("referringAttributes").getAsJsonArray();
		ArrayList<Attribute> referenced = new ArrayList<>();
		ArrayList<Attribute> referring = new ArrayList<>();
		Entity referencedEntity = null;
		
		for (JsonElement elem : referencedAttributes) {
//			System.out.println(elem);
			String[] names = elem.getAsString().split("/");
			Entity entity = new Entity(names[0]);
			Attribute attr = new Attribute(names[1]);
			
			referenced.add(attr);
			referencedEntity = entity;
		}
		
		for (JsonElement elem : referringAttributes) {
			Attribute attr = new Attribute(elem.getAsString());
			referring.add(attr);
		}
		
		Relation rel = new Relation(referring, referenced, referencedEntity);
		return rel;
	}
	
	// Entity
	public static JsonObject serializeEntity(Entity ent) {
		String name = ent.getName();
		String url = ent.getUrl();
		JsonObject obj = new JsonObject();
		JsonArray attrs = new JsonArray();
		JsonArray relations = new JsonArray();
		
		for (Node elem : ent.getChildren()) 
			if (elem instanceof Attribute) 
				attrs.add(serializeAttribute((Attribute) elem));

		for (Relation rel : ent.getRelations()) 
			relations.add(serializeRelation(rel));

		obj.addProperty("name", name);
		obj.addProperty("url", url);
		obj.add("attributes", attrs);
		obj.add("relations", relations);
		
		return obj;
	}

	public static Entity deserializeEntity(JsonObject obj) {

		String name = obj.get("name").getAsString();
		JsonArray attrs = obj.get("attributes").getAsJsonArray();
		JsonArray relations = obj.get("relations").getAsJsonArray();
		Entity entity = new Entity(name);

		for (JsonElement elem : attrs)
			entity.addChild(deserializeAttribute(elem.getAsJsonObject()));

		for (JsonElement elem : relations)
			entity.addRelation(deserializeRelation(elem.getAsJsonObject()));
		
		return entity;
	}

	// Inf Resource
	public static JsonObject serializeInfResource(InformationResource res) {
		String name = res.getName();
		JsonObject obj = new JsonObject();
		JsonArray packages = new JsonArray();

		for (Node elem : res.getChildren()) 
			if (elem instanceof Entity) 
				packages.add(serializePackage((Package) elem));

		obj.addProperty("name", name);
		obj.add("packages", packages);

		return obj;
	}

	public static InformationResource deserializeInfResource(JsonObject obj) {
		String name = obj.get("name").getAsString();
		JsonArray entities = obj.get("entities").getAsJsonArray();
		InformationResource res = new InformationResource(name);

		for (JsonElement elem : entities)
			res.addChild(deserializeEntity(elem.getAsJsonObject()));

		return res;
	}
	
	// Package
	public static JsonObject serializePackage(Package pack) {
		String name = pack.getName();
		String type = pack.getType();
		JsonObject obj = new JsonObject();
		JsonArray attrs = new JsonArray();

		for (Node elem : pack.getChildren()) 
			if (elem instanceof Entity) 
				attrs.add(serializeEntity((Entity) elem));

		obj.addProperty("name", name);
		obj.addProperty("type", type);
		obj.add("entities", attrs);

		return obj;
	}
}