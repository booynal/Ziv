package com.ziv.misc.storm.directgrouping;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.storm.shade.com.google.common.cache.Cache;
import org.apache.storm.shade.com.google.common.cache.CacheBuilder;
import org.apache.storm.shade.org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/**
 * Word Sum Bolt
 *
 * @author ziv
 * @date 2017年12月17日 下午10:31:53
 */
public class WordSumBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;

	private OutputCollector collector;
	private Cache<String, AtomicInteger> wordCache;

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		wordCache = CacheBuilder.newBuilder().maximumSize(1024).expireAfterWrite(3, TimeUnit.SECONDS).removalListener((removalNotification) -> {
			String key = (String) removalNotification.getKey();
			AtomicInteger sum = (AtomicInteger) removalNotification.getValue();
			System.out.println("word sum result : [" + key + "," + sum.get() + "]");
		}).build();
	}

	@Override
	public void execute(Tuple input) {
		try {
			String word = input.getString(0);
			int count = input.getInteger(1);
			if (StringUtils.isEmpty(word)) {
				return;
			}
			AtomicInteger counter = wordCache.getIfPresent(word);
			if (counter == null) {
				wordCache.put(word, new AtomicInteger(count));
			} else {
				counter.addAndGet(count);
			}
			collector.ack(input);
		} catch (Exception e) {
			collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}
}
