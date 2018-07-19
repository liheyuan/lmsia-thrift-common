package com.coder4.sbmvt.thrift.common;

import com.coder4.sbmvt.trace.TraceIdContext;
import com.coder4.sbmvt.trace.TraceIdUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author coder4
 */
public class TraceBinaryProtocol extends TBinaryProtocol {

    public static final short TRACE_ID_FIELD = Short.MAX_VALUE;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public TraceBinaryProtocol(TTransport trans) {
        super(trans);
    }

    public TraceBinaryProtocol(TTransport trans, boolean strictRead, boolean strictWrite) {
        super(trans, strictRead, strictWrite);
    }

    public TraceBinaryProtocol(TTransport trans, long stringLengthLimit,
                               long containerLengthLimit, boolean strictRead,
                               boolean strictWrite) {
        super(trans, stringLengthLimit, containerLengthLimit, strictRead, strictWrite);
    }

    @Override
    public void writeFieldStop() throws TException {
        // get traceId from context
        String traceId = TraceIdUtils.getTraceId();
        if (traceId == null || traceId.isEmpty()) {
            // generate new one if not avaliable
            traceId = TraceIdUtils.getTraceId();
        }
        // parse traceId
        TField field = new TField("", TType.STRING, TRACE_ID_FIELD);
        writeFieldBegin(field);
        writeString(traceId);
        writeFieldEnd();
        // super
        super.writeFieldStop();
    }

    @Override
    public TField readFieldBegin() throws TException {
        // super
        TField field = super.readFieldBegin();
        // read traceId
        while (true) {
            switch (field.id) {
                case TRACE_ID_FIELD:
                    if (field.type == TType.STRING) {
                        // set traceId to context
                        String traceId = readString();
                        TraceIdContext.setTraceId(traceId);
                        readFieldEnd();
                    } else {
                        TProtocolUtil.skip(this, field.type);
                        LOG.error("traceId field type is not string");
                    }
                    break;
                default:
                    return field;
            }

            field = super.readFieldBegin();
        }
    }

    public static class Factory extends TBinaryProtocol.Factory implements TProtocolFactory {

        public Factory() {
            super();
        }

        public Factory(boolean strictRead, boolean strictWrite) {
            super(strictRead, strictWrite);
        }

        public Factory(boolean strictRead, boolean strictWrite, long stringLengthLimit, long containerLengthLimit) {
            super(strictRead, strictWrite, stringLengthLimit, containerLengthLimit);
        }

        @Override
        public TProtocol getProtocol(TTransport trans) {
            TraceBinaryProtocol protocol =
                    new TraceBinaryProtocol(trans, stringLengthLimit_, containerLengthLimit_,
                            strictRead_, strictWrite_);

            return protocol;
        }
    }
}