/** Thrift specification for spring-yarn-thrift heartbeat protocol */
namespace  java   org.springframework.yarn.thrift.hb.gen

enum NodeType {
    CONTAINER = 0;
    ADMIN = 1;
}

struct NodeInfo {

    /** State of Node - any json object */
    1: optional string jsonData,

    /** Port of some other service that exposes an End point on the node */
    2: optional i32 auxEndPointPort1,

    /** Port of some other service that exposes an End point on the node */
    3: optional i32 auxEndPointPort2,

}


struct HeartbeatMessage {

    /** Slave Node id */
    1: required string nodeId,

    /** Slave hostname */
    2: required string host,

    /** Slave Node name */
    3: required NodeType nodeType,

    /** Slave Node Info */
    4: required NodeInfo nodeInfo,

    /** Port on which Command EndPoint will listen on*/
    5: optional i32 commandPort,

}

enum CommandMessageType {
    GENERIC = 0;
    DIE = 1;
}

struct HeartbeatCommandMessage {

    1: required CommandMessageType commandMessageType,

    2: optional string jsonData,

}

service HeartbeatEndPoint {

    /** Accept Heartbeat msg */
    bool acceptHeartbeat(1: string sessionId, 2: HeartbeatMessage heartbeatMessage)

}

service HeartbeatCommandEndPoint {

    /** Notify Slave of change in Heartbeat End point */
    bool changeEndPoint(1: string sessionId, 2: string host, 3: i32 port),

    /** Command */
    bool command(1: string sessionId, 2: HeartbeatCommandMessage heartbeatCommandMessage)

}
