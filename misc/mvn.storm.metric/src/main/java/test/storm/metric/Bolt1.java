package test.storm.metric;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.storm.metric.api.CountMetric;
import org.apache.storm.metric.api.MeanReducer;
import org.apache.storm.metric.api.MultiCountMetric;
import org.apache.storm.metric.api.ReducedMetric;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ziv on 2017/8/21.
 */
public class Bolt1 extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Bolt1.class);

	private Map<String, AtomicInteger> wordCountMap;
	private OutputCollector collector;
	private CountMetric totalCountMetric; // 统计总共的单词数
	private MultiCountMetric wordCountMetric; // 统计各个单词出现的次数
	private ReducedMetric avgLengthMetric; // 统计单词的平均长度

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		LOGGER.info("Bolt1.declareOutputFields()");
		declarer.declare(new Fields("word", "count"));
	}

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		LOGGER.info("Bolt1.prepare()");
		wordCountMap = new HashMap<>();
		this.collector = collector;
		totalCountMetric = new CountMetric();
		wordCountMetric = new MultiCountMetric();
		avgLengthMetric = new ReducedMetric(new MeanReducer());
		int timeBucketSizeInSecs = 5;
		context.registerMetric("test-totalCountMetric", totalCountMetric, timeBucketSizeInSecs);
		context.registerMetric("test-wordCountMetric", wordCountMetric, timeBucketSizeInSecs);
		context.registerMetric("test-avgLengthMetric", avgLengthMetric, timeBucketSizeInSecs);
	}

	@Override
	public void execute(Tuple input) {
		LOGGER.info("Bolt1.execute()");
		String word = input.getString(0);
		totalCountMetric.incr();
		wordCountMetric.scope(word).incr();
		avgLengthMetric.update(word.length());
		AtomicInteger count = wordCountMap.get(word);
		if (null == count) {
			wordCountMap.put(word, count = new AtomicInteger());
		}
		count.incrementAndGet();
		collector.emit(input, Utils.tuple(word, count.get()));
		collector.ack(input);
		LOGGER.debug(String.format("Bolt1.emit: (%s, %s)", word, count.get()));
	}

	@Override
	public void cleanup() {
		LOGGER.info("Bolt1.cleanup()");
	}
}
