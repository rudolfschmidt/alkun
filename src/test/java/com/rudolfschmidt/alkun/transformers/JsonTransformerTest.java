package com.rudolfschmidt.alkun.transformers;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class JsonTransformerTest {

	@Test
	public void flat() {
		FlatModel model = new FlatModel();
		model.str = UUID.randomUUID().toString();
		model.bool = true;
		model.date = new Date();
		model.integer = new Random().nextInt();
		String actual = JsonTransformer.render(model);
		assertEquals("{\"str\":\"" + model.str + "\",\"bool\":" + model.bool + ",\"integer\":" + model.integer + ",\"date\":" + model.date.getTime() + "}", actual);
	}

	@Test
	public void nested() {
		NestedModel nested = new NestedModel();
		nested.str = UUID.randomUUID().toString();
		nested.bool = true;
		nested.date = new Date();
		nested.integer = new Random().nextInt();
		NestedModel model = new NestedModel();
		model.str = UUID.randomUUID().toString();
		model.bool = true;
		model.date = new Date();
		model.integer = new Random().nextInt();
		model.model = nested;
		String actual = JsonTransformer.render(model);
		assertEquals("{\"str\":\"" + model.str + "\",\"bool\":" + model.bool + ",\"integer\":" + model.integer + ",\"date\":" + model.date.getTime() + ",\"model\":{\"str\":\"" + nested.str + "\",\"bool\":" + nested.bool + ",\"integer\":" + nested.integer + ",\"date\":" + nested.date.getTime() + "}}", actual);
	}

	@Test
	public void array() {
		FlatModel[] models = {new FlatModel(), new FlatModel()};
		models[0].str = UUID.randomUUID().toString();
		models[0].bool = true;
		models[0].date = new Date();
		models[0].integer = new Random().nextInt();
		models[1].str = UUID.randomUUID().toString();
		models[1].bool = true;
		models[1].date = new Date();
		models[1].integer = new Random().nextInt();
		String actual = JsonTransformer.render(models);
		assertEquals("[{\"str\":\"" + models[0].str + "\",\"bool\":" + models[0].bool + ",\"integer\":" + models[0].integer + ",\"date\":" + models[0].date.getTime() + "},{\"str\":\"" + models[1].str + "\",\"bool\":" + models[1].bool + ",\"integer\":" + models[1].integer + ",\"date\":" + models[1].date.getTime() + "}]", actual);
	}

	@Test
	public void list() {
		List<FlatModel> models = Arrays.asList(new FlatModel(), new FlatModel());
		models.get(0).str = UUID.randomUUID().toString();
		models.get(0).bool = true;
		models.get(0).date = new Date();
		models.get(0).integer = new Random().nextInt();
		models.get(1).str = UUID.randomUUID().toString();
		models.get(1).bool = true;
		models.get(1).date = new Date();
		models.get(1).integer = new Random().nextInt();
		String actual = JsonTransformer.render(models);
		assertEquals("[{\"str\":\"" + models.get(0).str + "\",\"bool\":" + models.get(0).bool + ",\"integer\":" + models.get(0).integer + ",\"date\":" + models.get(0).date.getTime() + "},{\"str\":\"" + models.get(1).str + "\",\"bool\":" + models.get(1).bool + ",\"integer\":" + models.get(1).integer + ",\"date\":" + models.get(1).date.getTime() + "}]", actual);
	}
}
