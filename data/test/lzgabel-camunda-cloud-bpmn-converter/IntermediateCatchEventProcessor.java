package cn.lzgabel.converter.processing.event;

import javax.xml.parsers.DocumentBuilderFactory;

import io.camunda.zeebe.model.bpmn.builder.zeebe.MessageBuilder;
import io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import cn.lzgabel.converter.bean.event.intermediate.IntermediateCatchEventDefinition;
import cn.lzgabel.converter.bean.event.intermediate.MessageIntermediateCatchEventDefinition;
import cn.lzgabel.converter.bean.event.intermediate.TimerIntermediateCatchEventDefinition;
import cn.lzgabel.converter.bean.event.start.EventType;
import cn.lzgabel.converter.processing.BpmnElementProcessor;
import io.camunda.zeebe.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;

/**
 * 〈功能简述〉<br>
 * 〈IntermediateCatchEvent节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class IntermediateCatchEventProcessor
    implements BpmnElementProcessor<IntermediateCatchEventDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(
      AbstractFlowNodeBuilder flowNodeBuilder, IntermediateCatchEventDefinition definition) {
    String nodeName = definition.getNodeName();
    String eventType = definition.getEventType();
    if (EventType.TIMER.isEqual(eventType)) {
      TimerIntermediateCatchEventDefinition timer =
          (TimerIntermediateCatchEventDefinition) definition;
      String timerDefinition = timer.getTimerDefinition();
      return flowNodeBuilder
          .intermediateCatchEvent()
          .timerWithDuration(timerDefinition)
          .getElement()
          .getId();
    } else if (EventType.MESSAGE.isEqual(eventType)) {
      MessageIntermediateCatchEventDefinition message =
          (MessageIntermediateCatchEventDefinition) definition;
      String messageName = message.getMessageName();
      String messageCorrelationKey = message.getCorrelationKey();
      if (StringUtils.isBlank(messageName) || StringUtils.isBlank(messageCorrelationKey)) {
        throw new RuntimeException("messageName/correlationKey 不能为空");
      }
      return flowNodeBuilder
          .intermediateCatchEvent()
          .name(nodeName)
          .message(
              messageBuilder -> {
                  // BLOCKTEST EVAL: https://github.com/lzgabel/camunda-cloud-bpmn-converter/blob/5a301723aa17c90a090c235f91d5491a1f7c249f/src/main/java/cn/lzgabel/converter/processing/event/IntermediateCatchEventProcessor.java#L47-L62
                  // @blocktest("test1").mock("messageBuilder.name(messageName)").given(messageBuilder, new MessageBuilder(new io.camunda.zeebe.model.bpmn.impl.BpmnModelInstanceImpl(new org.camunda.bpm.model.xml.impl.ModelImpl("foo"), new org.camunda.bpm.model.xml.impl.ModelBuilderImpl("foo"), new org.camunda.bpm.model.xml.impl.instance.DomDocumentImpl(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())), new io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl(new ModelTypeInstanceContext(null, null, null)))).given(messageName, "name").given("messageCorrelationKey", "=foo").expect(NullPointerException.class);
                  // @blocktest("test2").mock("messageBuilder.name(messageName)").given(messageBuilder, new MessageBuilder(new io.camunda.zeebe.model.bpmn.impl.BpmnModelInstanceImpl(new org.camunda.bpm.model.xml.impl.ModelImpl("foo"), new org.camunda.bpm.model.xml.impl.ModelBuilderImpl("foo"), new org.camunda.bpm.model.xml.impl.instance.DomDocumentImpl(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())), new io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl(new ModelTypeInstanceContext(null, null, null)))).given(messageName, "name").given("messageCorrelationKey", "foo").expect(NullPointerException.class);
                  // bNOTlocktest("test3").mock("messageBuilder.name(messageName)").given(messageBuilder, new MessageBuilder(new io.camunda.zeebe.model.bpmn.impl.BpmnModelInstanceImpl(new org.camunda.bpm.model.xml.impl.ModelImpl("foo"), new org.camunda.bpm.model.xml.impl.ModelBuilderImpl("foo"), new org.camunda.bpm.model.xml.impl.instance.DomDocumentImpl(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())), new io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl(new ModelTypeInstanceContext(null, null, null)))).given(messageName, "").given("messageCorrelationKey", "=foo").expect(NullPointerException.class);
                  // bNOTlocktest("test4").mock("messageBuilder.name(messageName)").given(messageBuilder, new MessageBuilder(new io.camunda.zeebe.model.bpmn.impl.BpmnModelInstanceImpl(new org.camunda.bpm.model.xml.impl.ModelImpl("foo"), new org.camunda.bpm.model.xml.impl.ModelBuilderImpl("foo"), new org.camunda.bpm.model.xml.impl.instance.DomDocumentImpl(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())), new io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl(new ModelTypeInstanceContext(null, null, null)))).given(messageName, "").given("messageCorrelationKey", "");
                  if (StringUtils.isNotBlank(messageName)) {
                  messageBuilder.name(messageName);
                }
                if (StringUtils.isNotBlank(messageCorrelationKey)) {
                  // The correlationKey is an expression that usually accesses a variable of the
                  // process instance
                  // that holds the correlation key of the message
                  // 默认如果没有 '=' 则自动拼上
                  if (StringUtils.startsWith(messageCorrelationKey, ZEEBE_EXPRESSION_PREFIX)) {
                    messageBuilder.zeebeCorrelationKey(messageCorrelationKey);
                  } else {
                    messageBuilder.zeebeCorrelationKeyExpression(messageCorrelationKey);
                  }
                }
              })
          .getElement()
          .getId();
    }
    return null;
  }
}
