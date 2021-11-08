#version 120

uniform sampler2D texture;
uniform vec2 texelSize;
uniform int bias;
uniform vec4 color;
uniform float size;
uniform float fade;

float normal(float value, float min, float max) {
    return ((value - min) / (max - min));
}

void main() {
    vec4 centerCol = texture2D(texture, gl_TexCoord[0].xy, bias);

    if(centerCol.a != 0) {
        gl_FragColor = color;
    } else {

        float alpha = 0;

        for (float x = -size; x < size; x++){
            for (float y = -size; y < size; y++){
                vec4 currentColor = texture2D(texture, gl_TexCoord[0].xy + vec2(texelSize.x * x, texelSize.y * y));

                if (currentColor.a != 0){
                    alpha += ((-clamp(normal(distance(vec2(x, y), vec2(0, 0)), 0, size), 0, 1)) + 1) * fade;
                }
            }
        }

        gl_FragColor = vec4(color.rgb, min(alpha, color.a));
    }
}