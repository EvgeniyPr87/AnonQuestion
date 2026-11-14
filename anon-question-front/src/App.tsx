import { useState, useEffect } from 'react'
import bridge from "@vkontakte/vk-bridge";
import {
  AppRoot, SplitLayout, SplitCol, Panel, PanelHeader,
  Group, Card, Div, Button, Placeholder, Spinner, ConfigProvider,
} from '@vkontakte/vkui'
import '@vkontakte/vkui/dist/vkui.css'


function App() {
  const [user, setUser]=useState<{id: number; first_name: string} | null>(null)
  const [error, setError]=useState<string | null>(null)

  useEffect(()=>{
    let isMounted=true;

    const timer=setTimeout(()=>{
    if(isMounted){
      console.log("Тфймер сработал, ВК молчит. Показываю интерфейс");
      setUser({id:0, first_name: 'Друг'});
    }

    }, 2000);


  bridge.send('VKWebAppInit').catch(console.error)

  bridge.send('VKWebAppGetUserInfo' )
  .then((data: any)=>{
    if(isMounted){
      clearTimeout(timer);
      setUser({id: data.id, first_name: data.first_name})
    }
    
  })
.catch((err)=>{
  console.console.error('VK Bridge error:', err);

  if(isMounted){
    clearTimeout(timer);
    setUser({id:0, first_name: 'Друг'});
  }

});
return ()=>{
  isMounted=false;
  clearTimeout(timer);
};

},[])

  return (
    <ConfigProvider>
      <AppRoot>
        <SplitLayout>
          <SplitCol>
           <Panel id="main">
           <PanelHeader>Анонимные вопросы</PanelHeader>
           {error && (
            <Div><Placeholder>{error} </Placeholder></Div>
           )}

           {!user && !error ? (
            <Div><Spinner size="l"/></Div>
           ) : user ? (
            <Group>
              <Placeholder>
                Привет, ${user.first_name}!<br/>
                Это твой тестовый экран. VK ID: {user.id}
              </Placeholder>
              <Card>
                <Div>
                  <Button size='l' stretched mode="primary" onClick={()=> alert('Кнопка работает!')}>
                    Скоро здесь будет твоя ссылка
                  </Button>
                </Div>
              </Card>
            </Group>
           ) : null}
           </Panel>
           </SplitCol>
           </SplitLayout>
           </AppRoot>
           </ConfigProvider>



  )
}

export default App
