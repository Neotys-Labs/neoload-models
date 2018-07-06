package com.neotys.neoload.model.listener;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.lang3.mutable.MutableInt;

import com.google.common.collect.Lists;

public class CurrentAndTotalCounter {

	private int current = 0;
	private Map<String, MutableInt> totalOccurencePerName = LazyMap.lazyMap(new HashMap<String, MutableInt>(), (Factory<MutableInt>)MutableInt::new);
	
	public void nextScript() {
		current = 0;
	}

	public int getCurrent() {
		return current;
	}

	public int getTotal() {
		int total = 0;
		for (final MutableInt occurence : totalOccurencePerName.values()) {
			total += occurence.intValue();
		}
		return total;
	}

	public void increment(final String name) {
		current = current + 1;
		totalOccurencePerName.get(name).increment();
	}

	public String getListSummary() {
		List<Map.Entry<String, MutableInt>> totalOccurencePerNameList = Lists.newArrayList(totalOccurencePerName.entrySet());
		Comparator<Entry<String, MutableInt>> reverseOrder = (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue());
		Collections.sort(totalOccurencePerNameList, reverseOrder);
		final StringBuilder summary = new StringBuilder();
		for (final Entry<String, MutableInt> entry : totalOccurencePerNameList) {
			summary.append("\t\t\t*").append(entry.getValue().intValue()).append(": ").append(entry.getKey()).append("\n");
		}
		return summary.toString();
	}
}
