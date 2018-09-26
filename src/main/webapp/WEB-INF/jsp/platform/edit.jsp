<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="../include.inc.jsp" %>
<!-- profile form -->
<form onsubmit="return false" autocomplete="off">
    <table class="formtable">
        <tr>
            <td class="flabel">平台名称</td>
            <td>
                <c:choose>
                    <c:when test="${empty platformInfo}">
                        <input name="pname" class="text w8" required>
                    </c:when>
                    <c:otherwise>
                        <input name="pname" class="text w8" value="${platformInfo.pname}">

                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="flabel w4">平台地址</td>
            <td>
                <c:choose>
                    <c:when test="${empty platformInfo}">
                        <input name="platformUrl" class="text w8" required>
                    </c:when>
                    <c:otherwise>
                        <input name="id" type=hidden value="${platformInfo.id}">
                        <input name="platformUrl" class="text w8" value="${platformInfo.platformUrl}">

                    </c:otherwise>
                </c:choose>

            </td>
        </tr>
        <tr>
            <td class="flabel">检测地址</td>
            <td>
                <c:choose>
                <c:when test="${empty platformInfo}">
                        <input name="alarmUrl" class="text w8" required>
                </c:when>
                    <c:otherwise>
                        <input name="alarmUrl"  class="text w8" value="${platformInfo.alarmUrl}">

                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</form>