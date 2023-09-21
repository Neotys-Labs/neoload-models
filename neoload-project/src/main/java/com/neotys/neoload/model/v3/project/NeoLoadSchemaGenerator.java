package com.neotys.neoload.model.v3.project;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class NeoLoadSchemaGenerator {

	private static final String OUTPUT_DIRECTORY = "D:\\wks\\github\\neoload-models\\neoload-project\\src\\main\\resources\\";

	static class GenerationException extends RuntimeException {

		public GenerationException() {
		}

		public GenerationException(String message) {
			super(message);
		}

		public GenerationException(String message, Throwable throwable) {
			super(message, throwable);
		}
	}

	/**
	 * <p>La DOCUMENTATION : https://victools.github.io/jsonschema-generator/#quot-additionalproperties-quot-keyword</p>
	 * EXEMPLES : https://github.com/victools/jsonschema-generator/tree/main/jsonschema-examples/src/main/java/com/github/victools/jsonschema/examples
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_7, OptionPreset.PLAIN_JSON);
		SchemaGeneratorConfig config = configBuilder.build();
		configBuilder.with(Option.DEFINITIONS_FOR_ALL_OBJECTS, Option.DEFINITIONS_FOR_MEMBER_SUPERTYPES);

		// Le nom de la prop est celui déclaré dans @JsonProperty et non le nom du champ en java
		configBuilder.forFields()
				.withPropertyNameOverrideResolver(field -> Optional.ofNullable(field.getAnnotationConsideringFieldAndGetter(JsonProperty.class))
						.map(JsonProperty::value)
						.orElse(null));

		// la propriété est requise que si elle a l'annotation RequiredCheck (il faut la rendre @Inherited pour qu'elle soit active dans la classe ImmutableXXX)
		// FIXME Ca marche pas pour les @Value.Default qui sont requis alors qu'ils ne devraient pas
		// FIXME le @JsonProperty sur les valeurs d'enums n'est pas lu. Ex: com.neotys.neoload.model.v3.project.server.Server.Scheme
		// FIXME certains champs supportent les types integer ET les string (Ex: Server Port) mais le générateur met seulement "String" alors qu'on peut mettre un number json et c'est valide
		// FIXME Ca ne marche pas car il y a des sérialiseurs custom que la lib ne voit pas :
		// 		- ServerDeserializer.java
		// 				=> Ca met une default value pour le port qui n'est plus obligatoire
		//				=> Les classes ne peuvent pas avoir de @JsonProperty mais elles ont un nom différent. Ex: Authentication.java => 3 subclass, parsing custom: ServerDeserializer.getAuthentication()
		// 	  - ConditionDeserializer.java
		//				=> Ca utilise ant pour lire la string
		// IMPROVEMENT: Sur le générateur initié par Paul à la main, il y a des "MinItems", des "default", des "title" et des en-têtes en début de fichier qui ne sont pas avec le générateur.
		// Mais on pourrais les avoir en configurant le générateur
		configBuilder.forFields()
				.withRequiredCheck(field -> field.getAnnotationConsideringFieldAndGetter(RequiredCheck.class) != null
						&& field.getAnnotationConsideringFieldAndGetter(Nullable.class) == null);

		// Ca permet de trouver les classes filles (les ImmutableXXX en partant des interfaces)
		configBuilder.forTypesInGeneral()
				.withSubtypeResolver(new ClassGraphSubtypeResolver());

		SchemaGenerator generator = new SchemaGenerator(config);
		JsonNode jsonSchema = generator.generateSchema(ImmutableProject.class);

		// Write to file
		final File outputFile = new File(OUTPUT_DIRECTORY + "as-code-generated.latest.schema.json");
		try (final FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			outputStream.write(jsonSchema.toPrettyString().getBytes());
			System.out.println("Output to file " + outputFile.getAbsolutePath());
		} catch (IOException e) {
			throw new GenerationException("Error while writing generated schema to file", e);
		}

	}

	/**
	 * Simple implementation of a reflection based subtype resolver, considering only subtypes from a certain package.
	 */
	static class ClassGraphSubtypeResolver implements SubtypeResolver {

		private final ClassGraph classGraphConfig;
		private ScanResult scanResult;

		ClassGraphSubtypeResolver() {
			this.classGraphConfig = new ClassGraph()
					.enableClassInfo()
					.enableInterClassDependencies()
					// in this example, only consider a certain set of potential subtypes
					.acceptPackages("com.neotys.neoload.model.v3.project");
		}

		private ScanResult getScanResult() {
			if (this.scanResult == null) {
				this.scanResult = this.classGraphConfig.scan();
			}
			return this.scanResult;
		}

		@Override
		public void resetAfterSchemaGenerationFinished() {
			if (this.scanResult != null) {
				this.scanResult.close();
				this.scanResult = null;
			}
		}

		@Override
		public List<ResolvedType> findSubtypes(ResolvedType declaredType, SchemaGenerationContext context) {
			if (declaredType.getErasedType() == Object.class) {
				return null;
			}
			ClassInfoList subtypes;
			if (declaredType.isInterface()) {
				subtypes = this.getScanResult().getClassesImplementing(declaredType.getErasedType());
			} else {
				subtypes = this.getScanResult().getSubclasses(declaredType.getErasedType());
			}
			if (!subtypes.isEmpty()) {
				TypeContext typeContext = context.getTypeContext();
				return subtypes.loadClasses(true)
						.stream()
						.map(subclass -> typeContext.resolveSubtype(declaredType, subclass))
						// Exclu les classes Json générées par Immutable et les interfaces des immutables qui faisaient doublon
						.filter(resolvedType -> !resolvedType.getErasedType().getSimpleName().equals("Json") && !resolvedType.isInterface())
						.collect(Collectors.toList());
			}
			return null;
		}
	}

}
