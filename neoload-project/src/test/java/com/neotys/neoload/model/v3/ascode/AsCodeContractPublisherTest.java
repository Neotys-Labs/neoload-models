package com.neotys.neoload.model.v3.ascode;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class AsCodeContractPublisherTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void publishesAFamilyWithoutTouchingTheFrozenSchemaOrOtherSteps() throws Exception {
		final Path repository = folder.getRoot().toPath();
		writeRepository(repository, false);
		final Path contract = writeContract(repository, true);

		final PublishResult result = new AsCodeContractPublisher().publish(repository, contract);

		assertThat(result.changed()).isTrue();
		assertThat(Files.readString(repository.resolve("schemas/v3.1/as-code.schema.json"))).contains("Open a widget.");
		final String open = Files.readString(repository.resolve("schemas/v3.2/as-code.schema.json"));
		assertThat(open).contains("Extra now.").contains("widget_ping").contains("#/definitions/user_paths/actions/transaction");
		assertThat(open).contains("\"stop_vu\"").contains("        \"rendezvous\"");
		assertThat(open).contains("\"transaction\": { \"$ref\": \"#/definitions/user_paths/actions/transaction\" }");
		final int properties = open.indexOf("\"properties\"");
		final int transaction = open.indexOf("\"transaction\"", properties);
		final int ping = open.indexOf("\"widget_ping\"", properties);
		final int delay = open.indexOf("\"delay\"", properties);
		assertThat(transaction).isLessThan(ping);
		assertThat(ping).isLessThan(delay);
		final String page = Files.readString(repository.resolve("doc/widget_actions.md"));
		assertThat(page).contains("Available settings are").contains("Extra now.").contains("## widget_ping").contains("`WidgetPing`");
		final String registry = Files.readString(repository.resolve(
				"neoload-project/src/main/java/com/neotys/neoload/model/v3/binding/serializer/CustomActionSteps.java"));
		assertThat(registry).contains(".put(\"widget_ping\", \"WidgetPing\")").contains(".put(\"widget_open\", \"WidgetOpen\")");
	}

	@Test
	public void secondPublishLeavesTheRepositoryUntouched() throws Exception {
		final Path repository = folder.getRoot().toPath();
		writeRepository(repository, false);
		final Path contract = writeContract(repository, true);
		new AsCodeContractPublisher().publish(repository, contract);
		final String schema = Files.readString(repository.resolve("schemas/v3.2/as-code.schema.json"));
		final String page = Files.readString(repository.resolve("doc/widget_actions.md"));

		final PublishResult result = new AsCodeContractPublisher().publish(repository, contract);

		assertThat(result.changed()).isFalse();
		assertThat(Files.readString(repository.resolve("schemas/v3.2/as-code.schema.json"))).isEqualTo(schema);
		assertThat(Files.readString(repository.resolve("doc/widget_actions.md"))).isEqualTo(page);
	}

	private static void writeRepository(final Path repository, final boolean withPing) throws Exception {
		Files.createDirectories(repository.resolve("schemas/v3.1"));
		Files.createDirectories(repository.resolve("schemas/v3.2"));
		Files.writeString(repository.resolve("schemas/v3.1/as-code.schema.json"), schema(false));
		Files.writeString(repository.resolve("schemas/v3.2/as-code.schema.json"), schema(withPing));
		Files.createDirectories(repository.resolve("doc"));
		Files.writeString(repository.resolve("doc/widget_actions.md"), page(withPing));
		final Path registry = repository.resolve("neoload-project/src/main/java/com/neotys/neoload/model/v3/binding/serializer");
		Files.createDirectories(registry);
		Files.writeString(registry.resolve("CustomActionSteps.java"), registry(withPing));
	}

	private static Path writeContract(final Path repository, final boolean withPing) throws Exception {
		final Path directory = repository.resolve("contract");
		Files.createDirectories(directory);
		final String pingStep = withPing ? ",\n    \"widget_ping\": \"WidgetPing\"" : "";
		Files.writeString(directory.resolve("family.json"), "{\n  \"schemaKey\": \"widget\",\n  \"schema\": \"schema.json\",\n"
				+ "  \"documentation\": \"doc/widget_actions.md\",\n  \"steps\": {\n    \"widget_open\": \"WidgetOpen\",\n"
				+ "    \"widget_close\": \"WidgetClose\"" + pingStep + "\n  }\n}\n");
		Files.writeString(directory.resolve("schema.json"), slice(withPing));
		return directory;
	}

	private static String registry(final boolean withPing) {
		final String ping = withPing ? "\n\t\t\t.put(\"widget_ping\", \"WidgetPing\")" : "";
		return "final class CustomActionSteps {\n\tstatic final Object STEPS = builder()\n"
				+ "\t\t\t.put(\"widget_open\", \"WidgetOpen\")\n\t\t\t.put(\"widget_close\", \"WidgetClose\")" + ping
				+ "\n\t\t\t.build();\n}\n";
	}

	private static String slice(final boolean withPing) {
		final String extra = withPing ? "Extra now." : "Extra.";
		final String narrative = withPing ? "Open the widget." : "Open a widget.";
		final String pingSlice = withPing ? ",\n  \"widget_ping\": { \"description\": \"Widget action WidgetPing. Ping.\" }" : "";
		return "{\n  \"action\": { \"type\": \"object\" },\n"
				+ "  \"parameter_names\": { \"name\": { \"const\": \"name\", \"description\": \"The name.\" } },\n"
				+ "  \"widget_open\": {\n    \"description\": \"Widget action WidgetOpen. " + narrative + "\",\n"
				+ "    \"properties\": {\n      \"parameters\": {\n        \"items\": { \"properties\": { \"name\": { \"anyOf\": [\n"
				+ "          { \"$ref\": \"#/definitions/user_paths/widget/parameter_names/name\" },\n"
				+ "          { \"const\": \"extra\", \"description\": \"" + extra + "\" }\n        ] } } },\n"
				+ "        \"allOf\": [ { \"contains\": { \"properties\": { \"name\": { \"const\": \"name\" } }, \"required\": [\"name\"] } } ]\n"
				+ "      }\n    }\n  },\n"
				+ "  \"widget_close\": { \"description\": \"Widget action WidgetClose. Close a widget.\" }" + pingSlice + "\n}\n";
	}

	private static String schema(final boolean withPing) {
		final String extra = withPing ? "Extra now." : "Extra.";
		final String narrative = withPing ? "Open the widget." : "Open a widget.";
		final String pingEnum = withPing ? ",\n                \"widget_ping\"" : "";
		final String pingProperty = withPing ? ",\n                \"widget_ping\": { \"$ref\": \"#/definitions/user_paths/widget/widget_ping\" }" : "";
		final String pingSlice = withPing ? ",\n        \"widget_ping\": { \"description\": \"Widget action WidgetPing. Ping.\" }" : "";
		return "{\n  \"definitions\": {\n    \"user_paths\": {\n      \"steps\": {\n        \"title\": \"Steps\",\n"
				+ "        \"items\": {\n          \"oneOf\": [\n            {\n              \"type\": \"string\",\n"
				+ "              \"enum\": [\n                \"stop_vu\",\n        \"rendezvous\",\n                \"widget_close\"" + pingEnum + "\n              ]\n"
				+ "            },\n            {\n              \"type\": \"object\",\n              \"minProperties\": 1,\n"
				+ "              \"maxProperties\": 1,\n              \"properties\": {\n"
				+ "                \"transaction\": { \"$ref\": \"#/definitions/user_paths/actions/transaction\" },\n"
				+ "                \"widget_open\": { \"$ref\": \"#/definitions/user_paths/widget/widget_open\" },\n"
				+ "                \"widget_close\": { \"$ref\": \"#/definitions/user_paths/widget/widget_close\" }" + pingProperty + ",\n"
				+ "                \"delay\": { \"$ref\": \"#/definitions/user_paths/actions/delay\" }\n"
				+ "              }\n            }\n          ]\n        }\n      },\n      \"widget\": {\n"
				+ "        \"action\": { \"type\": \"object\" },\n"
				+ "        \"parameter_names\": { \"name\": { \"const\": \"name\", \"description\": \"The name.\" } },\n"
				+ "        \"widget_open\": {\n          \"description\": \"Widget action WidgetOpen. " + narrative + "\",\n"
				+ "          \"properties\": {\n            \"parameters\": {\n              \"items\": { \"properties\": { \"name\": { \"anyOf\": [\n"
				+ "                { \"$ref\": \"#/definitions/user_paths/widget/parameter_names/name\" },\n"
				+ "                { \"const\": \"extra\", \"description\": \"" + extra + "\" }\n              ] } } },\n"
				+ "              \"allOf\": [ { \"contains\": { \"properties\": { \"name\": { \"const\": \"name\" } }, \"required\": [\"name\"] } } ]\n"
				+ "            }\n          }\n        },\n"
				+ "        \"widget_close\": { \"description\": \"Widget action WidgetClose. Close a widget.\" }" + pingSlice + "\n"
				+ "      }\n    }\n  }\n}\n";
	}

	private static String page(final boolean withPing) {
		final String narrative = withPing ? "Open the widget." : "Open a widget.";
		final String extra = withPing ? "Extra now." : "Extra.";
		final String pingRow = withPing ? "| [widget_ping](#widget_ping) | `WidgetPing` |\n" : "";
		final String pingSection = withPing ? "\n## widget_ping\n\nType `WidgetPing`. Ping.\n" : "";
		return "# Widgets\n\n#### Available settings are\n\nkept\n\n## Actions\n\n| Step | Type |\n|:-----|:-----|\n"
				+ "| [widget_open](#widget_open) | `WidgetOpen` |\n| [widget_close](#widget_close) | `WidgetClose` |\n" + pingRow
				+ "\n## widget_open\n\nType `WidgetOpen`. " + narrative + "\n\n| Parameter | Description | Required |\n"
				+ "|:----------|:------------|:--------:|\n| `name` | The name. | &#x2713; |\n| `extra` | " + extra + " | - |\n\n"
				+ "## widget_close\n\nType `WidgetClose`. Close a widget.\n" + pingSection;
	}
}
