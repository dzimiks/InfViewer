package metaschema;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Attribute;
import models.Entity;
import models.InformationResource;
import models.Package;
import models.Relation;
import models.Warehouse;
import models.datatypes.CharType;
import models.datatypes.DateType;
import models.datatypes.VarCharType;
import models.tree.Node;

public class Deserializer {

	JsonParser parser;
	JsonObject obj;
	
	public Deserializer(String json, Warehouse dest) {
		this.parser = new JsonParser();
		this.obj = parser.parse(json).getAsJsonObject();
	}
	
	public void deserializeToNode(JsonObject nodeJson, Node destination) {
		destination.setName(nodeJson.get("name").getAsString());
	}
	
	public void deserializeRelations(JsonArray relationsJson, Entity source) throws Exception {
		HashMap<String, Entity> entities = new HashMap<>();
		deserializeRelationsToEntity(relationsJson, source, entities);
	}
	
	public void deserializeRelationsToEntity(JsonArray relationsJson, Entity source, 
            								 HashMap<String, Entity> entities) throws Exception {
		HashSet<String> relationNames = new HashSet<>();
		
		for (JsonElement elem : relationsJson) {
			JsonObject relation = elem.getAsJsonObject();
			JsonArray referencedAttributesJson = relation.get("referencedAttributes").getAsJsonArray();
			JsonArray referringAttributesJson = relation.get("referringAttributes").getAsJsonArray();
			
			if (referencedAttributesJson.size() == 0) 
				throw new Exception("Entity: " + source.getName() + " has a relation with 0 referenced attributes");
			
			if (referringAttributesJson.size() == 0)
				throw new Exception("Entity: " + source.getName() + " has a relation with 0 referring attributes");
			
			if (referencedAttributesJson.size() != referringAttributesJson.size())
				throw new Exception("Entity: " + source.getName() + " has a relation with mismatching attribute counts");
			
			ArrayList<Attribute> referencedAttributes = new ArrayList<>();
			Entity referencedEntity = null;
			Entity parent = null;
			
			for (JsonElement referenced : referencedAttributesJson) {
				String[] names = referenced.getAsString().split("/");
				
				if (names.length != 2)
					throw new Exception("Referenced attribute " + String.join("/", names) + 
							" is not valid (expected format is 'EntityName/AttributeName')");
				
				if (!entities.containsKey(names[0]))
					throw new Exception("Entity '" + source.getName() + 
							" has a relation to unknown entity " + names[0]);
				
				referencedEntity = entities.get(names[0]);
				Attribute referencedAttribute = referencedEntity.findAttributeByName(names[1]);
				
				if (referencedAttribute == null) 
					throw new Exception("Referenced attribute " + String.join("/", names) + " does not exist");
				
				if (parent == null)
					parent = (Entity) referencedAttribute.getParent();
				else if (parent != (Entity) referencedAttribute.getParent())
					throw new Exception("Cannot have a composite key that refers to attributes in multiple tables on entity: " + source.getName());
				
				referencedAttributes.add(referencedAttribute);
			}
			
			ArrayList<Attribute> referringAttributes = new ArrayList<>();
			
			for (JsonElement referring : referringAttributesJson) {
				String referringAttributeName = referring.getAsString();
				Attribute referringAttribute = source.findAttributeByName(referringAttributeName);
				String rel = source.getName() + "/" + referringAttributeName;
				
				if (referringAttribute == null)
					throw new Exception("Referring attribute " + rel + " does not exist");
				
				referringAttributes.add(referringAttribute);
			}
			
			Relation r = new Relation(referencedAttributes, referringAttributes, referencedEntity);
			
			if (relationNames.contains(r.getName()))
				throw new Exception("Duplicate relation with name " + r.getName());
			
			relationNames.add(r.getName());
			source.addRelations(r);
			
			System.out.println("Referenced attributes: " + referencedAttributes);
			System.out.println("Referring attributes: " + referringAttributes);
			System.out.println("Relacija: " + r);
		}
	}
	
	public Attribute deserializeAttribute(JsonObject obj) throws Exception {
		HashMap<String, Attribute> attributes = new HashMap<>();
		return deserializeAttribute(obj, attributes);
	}
	
	public Attribute deserializeAttribute(JsonObject attributeJson, HashMap<String, Attribute> attributes) throws Exception {
		String type = attributeJson.get("type").getAsString();
		int length = attributeJson.get("length").getAsInt();
		boolean primaryKey = attributeJson.get("primaryKey").getAsBoolean();
		Class<?> valueClass = null;
		boolean mandatory = false;
		JsonElement mandatoryElement = attributeJson.get("mandatory");
		
		if (mandatoryElement != null)
			mandatory = mandatoryElement.getAsBoolean();
		
		switch (type) {
			case "char":
				valueClass = CharType.class;
				break;
			case "varchar":
				valueClass = VarCharType.class;
				break;
			case "datetime":
				valueClass = DateType.class;
				break;
			case "boolean":
				valueClass = Boolean.class;
				break;
			case "numeric":
				valueClass = Integer.class;
				break;
			default:
				throw new Exception("Unknown type: " + type);
		}
		
		Attribute attr = new Attribute("", primaryKey, length, valueClass, mandatory);
		deserializeToNode(attributeJson, attr);
		String name = attr.getName();
		
		if (attributes.containsKey(name))
			throw new Exception("Duplicate attribute: " + name);
		
		attributes.put(name, attr);
		return attr;
	}
	
	public Entity deserializeEntity(JsonObject obj) throws Exception {
		HashMap<String, Entity> entities = new HashMap<>();
		HashMap<String, JsonArray> relations = new HashMap<>();
		return deserializeEntity(obj, entities, relations);
	}
	
	public Entity deserializeEntity(JsonObject entityJson, HashMap<String, Entity> entities, HashMap<String, JsonArray> relationsJson) throws Exception {
		String url = Warehouse.getInstance().getLocation() + File.separatorChar + entityJson.get("url").getAsString();
		Entity entity = new Entity("", url);
		deserializeToNode(entityJson, entity);
		String name = entity.getName();
		
		if (entities.containsKey(name))
			throw new Exception("Duplicate entity: " + name);
		
		JsonArray attributesJson = entityJson.getAsJsonArray("attributes");
		HashMap<String, Attribute> attributes = new HashMap<>();
		
		for (JsonElement attributeJson : attributesJson)
			entity.addChild(deserializeAttribute(attributeJson.getAsJsonObject(), attributes));
		
		entities.put(name, entity);
		relationsJson.put(name, entityJson.getAsJsonArray("relations"));
		return entity;
	}
	
	public Package deserializePackage(JsonObject obj) throws Exception {
		HashMap<String, Package> packages = new HashMap<>();
		return deserializePackage(obj, packages);
	}
	
	public Package deserializePackage(JsonObject packageJson, HashMap<String, Package> packages) throws Exception {
		String type = packageJson.get("type").getAsString();
		Package pack = new Package("", type);
		deserializeToNode(packageJson, pack);
		String name = pack.getName();
		
		if (packages.containsKey(name))
			throw new Exception("Duplicate package: " + name);
	
		HashMap<String, Entity> entities = new HashMap<>();
		HashMap<String, JsonArray> relationsJson = new HashMap<>();
//		ArrayList<Package> subPackages = null;
		
		if (packageJson.has("entities")) {
			JsonArray entitiesJson = packageJson.getAsJsonArray("entities");
			
			for (JsonElement entityJson : entitiesJson) 
				pack.addChild(deserializeEntity(entityJson.getAsJsonObject(), entities, relationsJson));
		}
		
//		if (packageJson.has("packages")) {
////			subPackages = new ArrayList<>();
//
//			for (JsonElement subPackageJson : packageJson.getAsJsonArray("packages")) {
////				subPackages.add(deserializePackage(subPackageJson.getAsJsonObject(), packages));
//				Warehouse.getInstance().addChild(deserializePackage(subPackageJson.getAsJsonObject(), packages));
//			}
//		}
		
		for (Entry<String, JsonArray> relationsJsonEntry : relationsJson.entrySet()) {
			deserializeRelationsToEntity(relationsJsonEntry.getValue(), entities.get(relationsJsonEntry.getKey()), entities);
		}
		
		packages.put(name, pack);
		return pack;
	}
	
	public InformationResource deserializeInfResource(JsonObject obj) throws Exception {
		HashMap<String, InformationResource> infResources = new HashMap<>();
		return deserializeInfResource(obj, infResources);
	}
	
	public InformationResource deserializeInfResource(JsonObject infResJson, HashMap<String, InformationResource> infResources) throws Exception {
		InformationResource infRes = new InformationResource("");
		deserializeToNode(infResJson, infRes);
		String name = infRes.getName();
		
		if (infResources.containsKey(name))
			throw new Exception("Duplicate inf res: " + name);
		
		HashMap<String, Package> packages = new HashMap<>();
		
		if (infResJson.has("packages")) {
			JsonArray packagesJson = infResJson.getAsJsonArray("packages");
			
			for (JsonElement packageJson : packagesJson) 
				infRes.addChild(deserializePackage(packageJson.getAsJsonObject(), packages));
		}
		
		if (infResJson.has("infResources")) 
			for (JsonElement subInfResJson : infResJson.getAsJsonArray("infResources"))
				Warehouse.getInstance().addChild(deserializeInfResource(subInfResJson.getAsJsonObject(), infResources));
		
		infResources.put(name, infRes);
		return infRes;
	}
	
	public void deserialize(String json, Warehouse dest) throws Exception {
		deserializeToNode(obj, dest);
		dest.setDescription(obj.get("description").getAsString());
		dest.setLocation(obj.get("location").getAsString());
		dest.setType(obj.get("type").getAsString());
		
		if (obj.get("type").getAsString().equals("sql")) 
			dest.buildConnection();
		
		deserializeInfResource(obj);
	}
}