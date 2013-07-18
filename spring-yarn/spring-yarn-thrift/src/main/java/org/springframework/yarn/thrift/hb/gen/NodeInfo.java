/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.springframework.yarn.thrift.hb.gen;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeInfo implements org.apache.thrift.TBase<NodeInfo, NodeInfo._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("NodeInfo");

  private static final org.apache.thrift.protocol.TField JSON_DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("jsonData", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField AUX_END_POINT_PORT1_FIELD_DESC = new org.apache.thrift.protocol.TField("auxEndPointPort1", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField AUX_END_POINT_PORT2_FIELD_DESC = new org.apache.thrift.protocol.TField("auxEndPointPort2", org.apache.thrift.protocol.TType.I32, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new NodeInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new NodeInfoTupleSchemeFactory());
  }

  /**
   * State of Node - any json object
   */
  public String jsonData; // optional
  /**
   * Port of some other service that exposes an End point on the node
   */
  public int auxEndPointPort1; // optional
  /**
   * Port of some other service that exposes an End point on the node
   */
  public int auxEndPointPort2; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * State of Node - any json object
     */
    JSON_DATA((short)1, "jsonData"),
    /**
     * Port of some other service that exposes an End point on the node
     */
    AUX_END_POINT_PORT1((short)2, "auxEndPointPort1"),
    /**
     * Port of some other service that exposes an End point on the node
     */
    AUX_END_POINT_PORT2((short)3, "auxEndPointPort2");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // JSON_DATA
          return JSON_DATA;
        case 2: // AUX_END_POINT_PORT1
          return AUX_END_POINT_PORT1;
        case 3: // AUX_END_POINT_PORT2
          return AUX_END_POINT_PORT2;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __AUXENDPOINTPORT1_ISSET_ID = 0;
  private static final int __AUXENDPOINTPORT2_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.JSON_DATA,_Fields.AUX_END_POINT_PORT1,_Fields.AUX_END_POINT_PORT2};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.JSON_DATA, new org.apache.thrift.meta_data.FieldMetaData("jsonData", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.AUX_END_POINT_PORT1, new org.apache.thrift.meta_data.FieldMetaData("auxEndPointPort1", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.AUX_END_POINT_PORT2, new org.apache.thrift.meta_data.FieldMetaData("auxEndPointPort2", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(NodeInfo.class, metaDataMap);
  }

  public NodeInfo() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public NodeInfo(NodeInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetJsonData()) {
      this.jsonData = other.jsonData;
    }
    this.auxEndPointPort1 = other.auxEndPointPort1;
    this.auxEndPointPort2 = other.auxEndPointPort2;
  }

  public NodeInfo deepCopy() {
    return new NodeInfo(this);
  }

  @Override
  public void clear() {
    this.jsonData = null;
    setAuxEndPointPort1IsSet(false);
    this.auxEndPointPort1 = 0;
    setAuxEndPointPort2IsSet(false);
    this.auxEndPointPort2 = 0;
  }

  /**
   * State of Node - any json object
   */
  public String getJsonData() {
    return this.jsonData;
  }

  /**
   * State of Node - any json object
   */
  public NodeInfo setJsonData(String jsonData) {
    this.jsonData = jsonData;
    return this;
  }

  public void unsetJsonData() {
    this.jsonData = null;
  }

  /** Returns true if field jsonData is set (has been assigned a value) and false otherwise */
  public boolean isSetJsonData() {
    return this.jsonData != null;
  }

  public void setJsonDataIsSet(boolean value) {
    if (!value) {
      this.jsonData = null;
    }
  }

  /**
   * Port of some other service that exposes an End point on the node
   */
  public int getAuxEndPointPort1() {
    return this.auxEndPointPort1;
  }

  /**
   * Port of some other service that exposes an End point on the node
   */
  public NodeInfo setAuxEndPointPort1(int auxEndPointPort1) {
    this.auxEndPointPort1 = auxEndPointPort1;
    setAuxEndPointPort1IsSet(true);
    return this;
  }

  public void unsetAuxEndPointPort1() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __AUXENDPOINTPORT1_ISSET_ID);
  }

  /** Returns true if field auxEndPointPort1 is set (has been assigned a value) and false otherwise */
  public boolean isSetAuxEndPointPort1() {
    return EncodingUtils.testBit(__isset_bitfield, __AUXENDPOINTPORT1_ISSET_ID);
  }

  public void setAuxEndPointPort1IsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __AUXENDPOINTPORT1_ISSET_ID, value);
  }

  /**
   * Port of some other service that exposes an End point on the node
   */
  public int getAuxEndPointPort2() {
    return this.auxEndPointPort2;
  }

  /**
   * Port of some other service that exposes an End point on the node
   */
  public NodeInfo setAuxEndPointPort2(int auxEndPointPort2) {
    this.auxEndPointPort2 = auxEndPointPort2;
    setAuxEndPointPort2IsSet(true);
    return this;
  }

  public void unsetAuxEndPointPort2() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __AUXENDPOINTPORT2_ISSET_ID);
  }

  /** Returns true if field auxEndPointPort2 is set (has been assigned a value) and false otherwise */
  public boolean isSetAuxEndPointPort2() {
    return EncodingUtils.testBit(__isset_bitfield, __AUXENDPOINTPORT2_ISSET_ID);
  }

  public void setAuxEndPointPort2IsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __AUXENDPOINTPORT2_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case JSON_DATA:
      if (value == null) {
        unsetJsonData();
      } else {
        setJsonData((String)value);
      }
      break;

    case AUX_END_POINT_PORT1:
      if (value == null) {
        unsetAuxEndPointPort1();
      } else {
        setAuxEndPointPort1((Integer)value);
      }
      break;

    case AUX_END_POINT_PORT2:
      if (value == null) {
        unsetAuxEndPointPort2();
      } else {
        setAuxEndPointPort2((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case JSON_DATA:
      return getJsonData();

    case AUX_END_POINT_PORT1:
      return Integer.valueOf(getAuxEndPointPort1());

    case AUX_END_POINT_PORT2:
      return Integer.valueOf(getAuxEndPointPort2());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case JSON_DATA:
      return isSetJsonData();
    case AUX_END_POINT_PORT1:
      return isSetAuxEndPointPort1();
    case AUX_END_POINT_PORT2:
      return isSetAuxEndPointPort2();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof NodeInfo)
      return this.equals((NodeInfo)that);
    return false;
  }

  public boolean equals(NodeInfo that) {
    if (that == null)
      return false;

    boolean this_present_jsonData = true && this.isSetJsonData();
    boolean that_present_jsonData = true && that.isSetJsonData();
    if (this_present_jsonData || that_present_jsonData) {
      if (!(this_present_jsonData && that_present_jsonData))
        return false;
      if (!this.jsonData.equals(that.jsonData))
        return false;
    }

    boolean this_present_auxEndPointPort1 = true && this.isSetAuxEndPointPort1();
    boolean that_present_auxEndPointPort1 = true && that.isSetAuxEndPointPort1();
    if (this_present_auxEndPointPort1 || that_present_auxEndPointPort1) {
      if (!(this_present_auxEndPointPort1 && that_present_auxEndPointPort1))
        return false;
      if (this.auxEndPointPort1 != that.auxEndPointPort1)
        return false;
    }

    boolean this_present_auxEndPointPort2 = true && this.isSetAuxEndPointPort2();
    boolean that_present_auxEndPointPort2 = true && that.isSetAuxEndPointPort2();
    if (this_present_auxEndPointPort2 || that_present_auxEndPointPort2) {
      if (!(this_present_auxEndPointPort2 && that_present_auxEndPointPort2))
        return false;
      if (this.auxEndPointPort2 != that.auxEndPointPort2)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(NodeInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    NodeInfo typedOther = (NodeInfo)other;

    lastComparison = Boolean.valueOf(isSetJsonData()).compareTo(typedOther.isSetJsonData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetJsonData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.jsonData, typedOther.jsonData);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAuxEndPointPort1()).compareTo(typedOther.isSetAuxEndPointPort1());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAuxEndPointPort1()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.auxEndPointPort1, typedOther.auxEndPointPort1);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAuxEndPointPort2()).compareTo(typedOther.isSetAuxEndPointPort2());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAuxEndPointPort2()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.auxEndPointPort2, typedOther.auxEndPointPort2);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeInfo(");
    boolean first = true;

    if (isSetJsonData()) {
      sb.append("jsonData:");
      if (this.jsonData == null) {
        sb.append("null");
      } else {
        sb.append(this.jsonData);
      }
      first = false;
    }
    if (isSetAuxEndPointPort1()) {
      if (!first) sb.append(", ");
      sb.append("auxEndPointPort1:");
      sb.append(this.auxEndPointPort1);
      first = false;
    }
    if (isSetAuxEndPointPort2()) {
      if (!first) sb.append(", ");
      sb.append("auxEndPointPort2:");
      sb.append(this.auxEndPointPort2);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class NodeInfoStandardSchemeFactory implements SchemeFactory {
    public NodeInfoStandardScheme getScheme() {
      return new NodeInfoStandardScheme();
    }
  }

  private static class NodeInfoStandardScheme extends StandardScheme<NodeInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, NodeInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // JSON_DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.jsonData = iprot.readString();
              struct.setJsonDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // AUX_END_POINT_PORT1
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.auxEndPointPort1 = iprot.readI32();
              struct.setAuxEndPointPort1IsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // AUX_END_POINT_PORT2
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.auxEndPointPort2 = iprot.readI32();
              struct.setAuxEndPointPort2IsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, NodeInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.jsonData != null) {
        if (struct.isSetJsonData()) {
          oprot.writeFieldBegin(JSON_DATA_FIELD_DESC);
          oprot.writeString(struct.jsonData);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetAuxEndPointPort1()) {
        oprot.writeFieldBegin(AUX_END_POINT_PORT1_FIELD_DESC);
        oprot.writeI32(struct.auxEndPointPort1);
        oprot.writeFieldEnd();
      }
      if (struct.isSetAuxEndPointPort2()) {
        oprot.writeFieldBegin(AUX_END_POINT_PORT2_FIELD_DESC);
        oprot.writeI32(struct.auxEndPointPort2);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class NodeInfoTupleSchemeFactory implements SchemeFactory {
    public NodeInfoTupleScheme getScheme() {
      return new NodeInfoTupleScheme();
    }
  }

  private static class NodeInfoTupleScheme extends TupleScheme<NodeInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, NodeInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetJsonData()) {
        optionals.set(0);
      }
      if (struct.isSetAuxEndPointPort1()) {
        optionals.set(1);
      }
      if (struct.isSetAuxEndPointPort2()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetJsonData()) {
        oprot.writeString(struct.jsonData);
      }
      if (struct.isSetAuxEndPointPort1()) {
        oprot.writeI32(struct.auxEndPointPort1);
      }
      if (struct.isSetAuxEndPointPort2()) {
        oprot.writeI32(struct.auxEndPointPort2);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, NodeInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.jsonData = iprot.readString();
        struct.setJsonDataIsSet(true);
      }
      if (incoming.get(1)) {
        struct.auxEndPointPort1 = iprot.readI32();
        struct.setAuxEndPointPort1IsSet(true);
      }
      if (incoming.get(2)) {
        struct.auxEndPointPort2 = iprot.readI32();
        struct.setAuxEndPointPort2IsSet(true);
      }
    }
  }

}

