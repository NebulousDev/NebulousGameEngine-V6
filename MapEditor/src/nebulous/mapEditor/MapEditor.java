package nebulous.mapEditor;

import org.joml.Vector2f;
import org.joml.Vector3f;

import nebulous.Game;
import nebulous.component.Level2D;
import nebulous.component.TileMap;
import nebulous.graphics.Camera;
import nebulous.graphics.RenderEngine;
import nebulous.graphics.primatives.Mesh;
import nebulous.graphics.primatives.Texture;
import nebulous.logic.Input;
import nebulous.utils.PositionHelper;

public class MapEditor extends Game{

	@Override
	public void preInit() {
		window.setSize(720, 480);
	}

	@Override
	public void init() {
		window.printGLStats();
		addLevel("mapEditor", new MapLevel());
		loadLevel("mapEditor");
	}

	@Override
	public void update(Game game, double delta) {
		
	}
	
	
	// MAP LEVEL
	
	public class MapLevel extends Level2D {
		
		private TileMap map1 = null;
		private MouseEntity mouseEntity = null;

		@Override
		public void init(Game game) {
			camera.setPerspective(Camera.PERSPECTIVE);
			camera.setPosition(0f, 0f, 10f);
			
			map1 = new TileMap(Texture.UNKNOWN, 32, 32, false);
			
			mouseEntity = new MouseEntity(Mesh.PLANE(Texture.UNKNOWN2), 0, 0);
			
			addTileMap("map1", map1);
			addEntity("mouseEntity", mouseEntity);
		}

		@Override
		public void onLoad() {
			
		}
		
		Vector2f previousMousePos = new Vector2f(0);
		Vector2f currentMousePos  = new Vector2f(0);

		@Override
		public void update(Game game, double delta) {
			
			// MOUSE DRAG
			
			float width = game.getWindow().getWidth();
			float height = game.getWindow().getHeight();
			
			previousMousePos = currentMousePos;
			currentMousePos = new Vector2f((float)Input.mousePosX, (float)Input.mousePosY);
			
			if(Input.isKeyHeld(Input.KEY_LEFT_SHIFT)) {
				if(Input.isMouseButtonHeld(0)){
					
					float differenceX = previousMousePos.x - currentMousePos.x;
					float differenceY = previousMousePos.y - currentMousePos.y;
					
					camera.move(differenceX / 30, -differenceY  / 30, 0);
				}
			}
			
			if(!Input.isKeyHeld(Input.KEY_LEFT_SHIFT)) {
				Vector3f pos = PositionHelper.toWorldSpace3D(map1, game.getWindow(), camera, (float) Input.mousePosX, (float) Input.mousePosY, camera.getPosition().z);
				System.out.println("-------\nX - " + pos.x);
				System.out.println("Y - " + pos.y);
				System.out.println("Z - " + pos.z);
				
				if(Input.isMouseButtonHeld(0)) {
					map1.setTile((int)(pos.x + 0.5f), (int)(pos.y + 0.5f), Texture.UNKNOWN2);
				}
				
				if(Input.isMouseButtonHeld(1)) {
					map1.setTile((int)(pos.x + 0.5f), (int)(pos.y + 0.5f), null);
				}
				
			}
			
			controlCamera();
		}
		
		float scrollVelocity = 0;
		
		public void controlCamera() {
			
			if(Input.isKeyHeld(Input.KEY_G)) camera.setPosition(camera.getPosition().add(new Vector3f(0,0,0.1f)));
			if(Input.isKeyHeld(Input.KEY_H)) camera.setPosition(camera.getPosition().add(new Vector3f(0,0,-0.1f)));
			if(Input.isKeyHeld(Input.KEY_E)) camera.setRotation(camera.getRotation().add(new Vector3f(0,0.1f,0)));
			if(Input.isKeyHeld(Input.KEY_R)) camera.setRotation(camera.getRotation().add(new Vector3f(0,-0.1f,0)));
			
			if(Input.isKeyHeld(Input.KEY_P)) camera.setRotation(0,0,0);
			
			if(Input.isMouseScrollIncrease()){ 
				scrollVelocity = -60;
			}
			
			if(Input.isMouseScrollDecrease()){
				scrollVelocity = 60;
			}
			
			if(scrollVelocity > 0) {
				camera.setPosition(camera.getPosition().add(new Vector3f(0, 0, 0.005f * scrollVelocity)));
				scrollVelocity -= 2;
			}
			
			if(scrollVelocity < 0) {
				camera.setPosition(camera.getPosition().add(new Vector3f(0, 0, 0.005f * scrollVelocity)));
				scrollVelocity += 2;
			}
			
		}

		@Override
		public void onUnload() {
			
		}
		
		@Override
		public void render(RenderEngine renderer) {
			super.render(renderer);
			
//			mouseEntity.setPosition(camera.getPosition().x, camera.getPosition().y);
		}
		
	}
	
	
	public static void main(String[] args) {
		new MapEditor();
	}
	

}