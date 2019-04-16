package com.rs.utils.pathfinder;

public class Routes {

	/*
	 * static final void findGroundItemRoute(int paramInt1, int paramInt2) { try
	 * { if (client.gametype != Class411.game_stellardawn) findRoute(paramInt1,
	 * paramInt2, false, createEntityStrategy(paramInt1, paramInt2, 1, 1, 0));
	 * else if (!findRoute(paramInt1, paramInt2, false,
	 * createEntityStrategy(paramInt1, paramInt2, 1, 1, 0)))
	 * findRoute(paramInt1, paramInt2, false,
	 * createRectOverlapStrategy(paramInt1, paramInt2, 1, 1)); } catch
	 * (RuntimeException localRuntimeException) { throw
	 * Class346.method4175(localRuntimeException, "pp.jv(" + ')'); } }
	 * 
	 * static final void findObjectRoute(int paramInt1, int paramInt2, long
	 * paramLong) { try { int i = (int)paramLong >> 14 & 0x1F; int j =
	 * (int)paramLong >> 20 & 0x3; int k = (int)(paramLong >>> 32) & 0x7FFFFFFF;
	 * GameObjectType localGameObjectType =
	 * (GameObjectType)Class422_Sub20.method5701
	 * (ExactStrategy.method4108(114624527), i, (byte) 2); PathStrategy
	 * localPathStrategy; if ((GameObjectType.T10 == localGameObjectType) ||
	 * (GameObjectType.T11 == localGameObjectType) || (GameObjectType.T22 ==
	 * localGameObjectType)) { ObjectDefinitions localObjectDefinitions =
	 * client.aClass283_8716.method2641(-1208362615).getObjectDefinitions(k);
	 * int m; int n; if ((j == 0) || (j == 2)) { m =
	 * localObjectDefinitions.sizeX * -1125834887; n =
	 * localObjectDefinitions.sizeY * -565161399; } else { m =
	 * localObjectDefinitions.sizeY * -565161399; n =
	 * localObjectDefinitions.sizeX * -1125834887; } localPathStrategy =
	 * createObjectCloseStrategy(paramInt1, paramInt2, m, n, GameObjectType.T0,
	 * 0); } else if (Class82_Sub9.isWall(localGameObjectType.type *
	 * -1976050083, (byte) 28)) { localPathStrategy =
	 * createObjectCloseStrategy(paramInt1, paramInt2, 0, 0,
	 * localGameObjectType, j); } else { localPathStrategy =
	 * createObjectExactStrategy(paramInt1, paramInt2, 0, 0,
	 * localGameObjectType, j); } findRoute(paramInt1, paramInt2, true,
	 * localPathStrategy); //System.err.println("id:" + k + ", type:" + i +
	 * ", rotation:" + j + ", ctype:" +
	 * client.aClass283_8716.method2641(-1208362615
	 * ).getObjectDefinitions(k).clip_type); } catch (RuntimeException
	 * localRuntimeException) { throw Class346.method4175(localRuntimeException,
	 * "du.jd(" + ')'); } }
	 * 
	 * static final boolean findRoute(int paramInt1, int paramInt2, boolean
	 * paramBoolean, PathStrategy paramPathStrategy) { try { int i =
	 * Class287.myPlayer.scenePositionXQueue[0]; int j =
	 * Class287.myPlayer.scenePositionYQueue[0]; if ((i < 0) || (i >=
	 * client.aClass283_8716.method2629(-2029828730)) || (j < 0) || (j >=
	 * client.aClass283_8716.method2630(911412275))) return false; if
	 * ((paramInt1 < 0) || (paramInt1 >=
	 * client.aClass283_8716.method2629(-2140756422)) || (paramInt2 < 0) ||
	 * (paramInt2 >= client.aClass283_8716.method2630(1432313507))) return
	 * false; int k = client.lastPathStepsCount =
	 * Class298_Sub37.calculateRoute(i, j, Class287.myPlayer.getSize(),
	 * paramPathStrategy,
	 * client.aClass283_8716.getSceneClipDataPlane(Class287.myPlayer.plane),
	 * paramBoolean, client.pathBufferX, client.pathBufferY); if (k < 1) return
	 * false; Class3.anInt62 = client.pathBufferX[(k - 1)] * -1129029761;
	 * Class3.anInt54 = client.pathBufferY[(k - 1)] * -1835291189;
	 * Class3.aBoolean63 = false; Class319.method3904(-2054792212); return true;
	 * } catch (RuntimeException localRuntimeException) { throw
	 * Class346.method4175(localRuntimeException, "yt.jz(" + ')'); } }
	 * 
	 * public static PathStrategy createObjectCloseStrategy(int paramInt1, int
	 * paramInt2, int paramInt3, int paramInt4, GameObjectType
	 * paramGameObjectType, int paramInt5) { try {
	 * Class315.objcheckstrategyclosest.toX = (paramInt1 * -760677635);
	 * Class315.objcheckstrategyclosest.toY = (paramInt2 * 167105303);
	 * Class315.objcheckstrategyclosest.sizeX = (paramInt3 * -1544157451);
	 * Class315.objcheckstrategyclosest.sizeY = (paramInt4 * -1468199503);
	 * Class315.objcheckstrategyclosest.aClass424_7712 = paramGameObjectType;
	 * Class315.objcheckstrategyclosest.anInt7711 = (paramInt5 * 393356885);
	 * return Class315.objcheckstrategyclosest; } catch (RuntimeException
	 * localRuntimeException) { throw Class346.method4175(localRuntimeException,
	 * "acp.i(" + ')'); } }
	 * 
	 * public static PathStrategy createObjectExactStrategy(int paramInt1, int
	 * paramInt2, int paramInt3, int paramInt4, GameObjectType
	 * paramGameObjectType, int paramInt5) { try {
	 * Class315.objcheckstrategyexact.toX = (paramInt1 * -760677635);
	 * Class315.objcheckstrategyexact.toY = (paramInt2 * 167105303);
	 * Class315.objcheckstrategyexact.sizeX = (paramInt3 * -1544157451);
	 * Class315.objcheckstrategyexact.sizeY = (paramInt4 * -1468199503);
	 * Class315.objcheckstrategyexact.aClass424_7713 = paramGameObjectType;
	 * Class315.objcheckstrategyexact.anInt7714 = (paramInt5 * -2142070477);
	 * return Class315.objcheckstrategyexact; } catch (RuntimeException
	 * localRuntimeException) { throw Class346.method4175(localRuntimeException,
	 * "if.k(" + ')'); } }
	 * 
	 * public static PathStrategy createRectOverlapStrategy(int paramInt1, int
	 * paramInt2, int paramInt3, int paramInt4) { try {
	 * Class315.rectoverlapstrategy.toX = (paramInt1 * -760677635);
	 * Class315.rectoverlapstrategy.toY = (paramInt2 * 167105303);
	 * Class315.rectoverlapstrategy.sizeX = (paramInt3 * -1544157451);
	 * Class315.rectoverlapstrategy.sizeY = (paramInt4 * -1468199503); return
	 * Class315.rectoverlapstrategy; } catch (RuntimeException
	 * localRuntimeException) { throw Class346.method4175(localRuntimeException,
	 * "ls.f(" + ')'); } }
	 * 
	 * public static PathStrategy createEntityStrategy(int paramInt1, int
	 * paramInt2, int paramInt3, int paramInt4, int paramInt5) { try {
	 * Class315.entitystrategy.toX = (paramInt1 * -760677635);
	 * Class315.entitystrategy.toY = (paramInt2 * 167105303);
	 * Class315.entitystrategy.sizeX = (paramInt3 * -1544157451);
	 * Class315.entitystrategy.sizeY = (paramInt4 * -1468199503);
	 * Class315.entitystrategy.anInt7715 = (paramInt5 * 89792661); return
	 * Class315.entitystrategy; } catch (RuntimeException localRuntimeException)
	 * { throw Class346.method4175(localRuntimeException, "op.a(" + ')'); } }
	 */

	public static PathStrategy createExactStrategy(int paramInt1, int paramInt2) {
		try {

			Class315.exactdestinationstrategy.toX = (paramInt1 * -760677635);
			Class315.exactdestinationstrategy.toY = (paramInt2 * 167105303);
			Class315.exactdestinationstrategy.sizeX = -1544157451;
			Class315.exactdestinationstrategy.sizeY = -1468199503;
			return Class315.exactdestinationstrategy;
		} catch (RuntimeException localRuntimeException) {
			throw Class346.method4175(localRuntimeException, "op.a(" + ')');
		}
	}

	/*
	 * public static PathStrategy createLobbyNpcsPathStrategy(int paramInt1, int
	 * paramInt2, int paramInt3, int paramInt4, int paramInt5) { try {
	 * Class315.lobbynpcsstrategy.toX = (paramInt1 * -760677635);
	 * Class315.lobbynpcsstrategy.toY = (paramInt2 * 167105303);
	 * Class315.lobbynpcsstrategy.sizeX = (paramInt3 * -1544157451);
	 * Class315.lobbynpcsstrategy.sizeY = (paramInt4 * -1468199503);
	 * Class315.lobbynpcsstrategy.routeType = (paramInt5 * -1073204575); return
	 * Class315.lobbynpcsstrategy; } catch (RuntimeException
	 * localRuntimeException) { throw Class346.method4175(localRuntimeException,
	 * "iu.p(" + ')'); } }
	 * 
	 * public static void sendLastWalkPathAsStdwalk() { if (!Loader.useRoute)
	 * return;
	 * 
	 * @SuppressWarnings("unused") int i = client.lastPathStepsCount; if (i > 0)
	 * { Class298_Sub36 localClass298_Sub36 =
	 * Class18.method359(OutcommingPacket.WALKING_PACKET,
	 * client.aClass25_8711.aClass449_330, (byte) 51);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(i * 2 + 5);
	 * Class341 localClass341 = client.aClass283_8716.method2628(681479919);
	 * localClass298_Sub36
	 * .aClass298_Sub53_Sub2_7396.writeShort128(localClass341.gameSceneBaseX *
	 * -1760580017);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.write128Byte(Class151
	 * .method1644(-545107710) ? 1 : 0, (byte) 1);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396
	 * .writeShort128(localClass341.gameSceneBaseY * 283514611); for (int j = i
	 * - 1; j >= 0; j--) {
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte
	 * (Class285.routeFinderXArray[j]);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396
	 * .writeByte(Class285.routeFinderYArray[j]); }
	 * client.aClass25_8711.method390(localClass298_Sub36, (byte) -115); } }
	 * 
	 * public static void sendLastWalkPathAsMinimapWalk() { if
	 * (!Loader.useRoute) return;
	 * 
	 * @SuppressWarnings("unused") int i = client.lastPathStepsCount; if (i > 0)
	 * { Class298_Sub36 localClass298_Sub36 =
	 * Class18.method359(OutcommingPacket.MINI_WALKING_PACKET,
	 * client.aClass25_8711.aClass449_330, (byte) 28);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(i * 2 + 5);
	 * Class341 localClass341 = client.aClass283_8716.method2628(681479919);
	 * localClass298_Sub36
	 * .aClass298_Sub53_Sub2_7396.writeShort128(localClass341.gameSceneBaseX *
	 * -1760580017);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.write128Byte(Class151
	 * .method1644(-545107710) ? 1 : 0, (byte) 1);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396
	 * .writeShort128(localClass341.gameSceneBaseY * 283514611); for (int j = i
	 * - 1; j >= 0; j--) {
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte
	 * (Class285.routeFinderXArray[j]);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396
	 * .writeByte(Class285.routeFinderYArray[j]); }
	 * client.aClass25_8711.method390(localClass298_Sub36, (byte) -115); } }
	 * 
	 * public static void sendPlainStdWalk(int paramInt1, int paramInt2) { if
	 * (Loader.useRoute) return; Class298_Sub36 localClass298_Sub36 =
	 * Class18.method359(OutcommingPacket.WALKING_PACKET,
	 * client.aClass25_8711.aClass449_330,(byte) 51);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(7); Class341
	 * localClass341 = client.aClass283_8716.method2628(681479919);
	 * localClass298_Sub36
	 * .aClass298_Sub53_Sub2_7396.writeShort128(localClass341.gameSceneBaseX *
	 * -1760580017);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.write128Byte(Class151
	 * .method1644(-545107710) ? 1 : 0, (byte) 1);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396
	 * .writeShort128(localClass341.gameSceneBaseY * 283514611);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(paramInt1);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(paramInt2);
	 * client.aClass25_8711.method390(localClass298_Sub36, (byte) -115); }
	 * 
	 * public static void sendPlainMinimapWalk(int paramInt1, int paramInt2) {
	 * if (Loader.useRoute) return; Class298_Sub36 localClass298_Sub36 =
	 * Class18.method359(OutcommingPacket.MINI_WALKING_PACKET,
	 * client.aClass25_8711.aClass449_330, (byte) 28);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(7); Class341
	 * localClass341 = client.aClass283_8716.method2628(681479919);
	 * localClass298_Sub36
	 * .aClass298_Sub53_Sub2_7396.writeShort128(localClass341.gameSceneBaseX *
	 * -1760580017);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.write128Byte(Class151
	 * .method1644(-545107710) ? 1 : 0, (byte) 1);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396
	 * .writeShort128(localClass341.gameSceneBaseY * 283514611);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(paramInt1);
	 * localClass298_Sub36.aClass298_Sub53_Sub2_7396.writeByte(paramInt2);
	 * client.aClass25_8711.method390(localClass298_Sub36, (byte) -115); }
	 */
}
