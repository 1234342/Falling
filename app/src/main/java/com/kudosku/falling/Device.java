package com.kudosku.falling;

import android.content.Context;

public class Device {

    private Context m_context;
    private RendererInfo m_rendererInfo;

    public Device(Context context) {
        m_context = context;
        m_rendererInfo = new RendererInfo();
    }

    public void setRendererType() {
        if (m_rendererInfo.hasES20(m_context))
            m_rendererInfo.setType(RendererInfo.RendererType.OPENGLES_20);
        m_rendererInfo.setType(RendererInfo.RendererType.ANDROIDSURFACE);
        // m_rendererInfo.setType(RendererInfo.RendererType.OPENGLES_10);
    }
    public void setRendererType(int rendererType) {
        m_rendererInfo.setType(rendererType);
    }

    public int getRendererType() {
        return m_rendererInfo.getType();
    }
    public String getRendererTypeForStr() {
        return  m_rendererInfo.getTypeForStr();
    }

}
